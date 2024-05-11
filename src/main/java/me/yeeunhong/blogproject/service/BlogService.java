package me.yeeunhong.blogproject.service;

import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.*;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    // 테이블에 저장되어 있는 모든 데이터 조회
    public List<Article> findAll() {
        List<Article> articleList = blogRepository.findAll();
        for (Article article : articleList) {
            // 삭제된 게시글은 포함하지 않는다.
            if (article.getDeletedAt() != null) {
                articleList.remove(article);
            }
        }
        return articleList;
    }

    public List<ArticleResponse> findArticlesWithParams(String sortingTypeInput, String title) {
       if ((title != null) & (sortingTypeInput != null)) {
           if (sortingTypeInput.contains("asc")) {
               return getArticlesByTitleSorted(title, false);
           } else {
               // default descending
               return getArticlesByTitleSorted(title, true);
           }
       } else {
           if (sortingTypeInput.contains("asc")) {
               return getArticlesOrderBy(false);
           } else {
               // default descending
               return getArticlesOrderBy(true);
           }
       }
    }

    // createdAt을 기준으로 클라이언트의 요청에 의해 내림차순 혹은 오름차순으로 나타낼 수 있도록 구현
    public List<ArticleResponse> getArticlesOrderBy(boolean isDescending) {
        if (isDescending) {
            return blogRepository.findAllByOrderByCreatedAtDesc().stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        } else {
            return blogRepository.findAllByOrderByCreatedAtAsc().stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        }
    }

    public List<ArticleResponse> getArticlesByTitleSorted(String title, boolean isDescending) {
        if (isDescending) {
            return blogRepository.findAllByTitleContainsOrderByCreatedAtDesc(title).stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        } else {
            return blogRepository.findAllByTitleContainsOrderByCreatedAtAsc(title).stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        }
    }

    // JPA의 findById() 메서드를 사용해 ID를 받아 엔티티를 조회하고 없으면 illegalArgumentException 예외 발생
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
    }

    public UpdateArticleResponse getUpdateMessage(long id) {
        Article article = findById(id);
        // 수정 가능일을 현재날짜 기준으로 계산해서 같이 보여주기
        String message = article.getUpdateMessage();
        return new UpdateArticleResponse(article, message);
    }

    @Transactional
    public ArticleResponseInterface updateArticleWithValidation(long id, UpdateArticleRequest request) {
        Article articleToUpdate = findById(id);

        long daysToUpdate = articleToUpdate.numDaysFromUpdated();

        if (daysToUpdate < 9) {
            // 생성일 기준으로 10일이 되지 않았다면 수정 가능
            Article updatedArticle = update(articleToUpdate, request);
            return new ArticleResponse(updatedArticle);

        } else if (daysToUpdate == 9) {
            // 생성일 9일째 되었을 때 하루가 지나면 수정을 못한다는 알람을 처리
            Article updatedArticle = update(articleToUpdate, request);
            String message = "1일 후에는 수정할 수 없습니다.";
            return new UpdateArticleResponse(updatedArticle, message);

        } else {
            // 생성일 10일 이상이 되었다면 수정 불가
            return new ArticleResponse(articleToUpdate);
        }
    }

    @Transactional // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    // 엔티티의 필드 값이 바뀌면 중간에 에러가 발생해도 제대로 된 값 수정을 보장
    public Article update(Article updateArticle, UpdateArticleRequest request) {

        updateArticle.update(
                request.getTitle(),
                request.getContent(),
                request.getEmail(),
                request.getPhoneNum(),
                request.getAuthor(),
                request.getPassword()
        );

        return updateArticle;
    }

    @Transactional
    public Article softDelete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
        article.deleteSoftly();
        return article;
    }

    public void hardDelete(long id) {
        blogRepository.deleteById(id);
    }

}
