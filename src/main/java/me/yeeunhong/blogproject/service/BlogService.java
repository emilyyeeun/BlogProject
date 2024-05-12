package me.yeeunhong.blogproject.service;

import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.*;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    /*
    테이블에 저장되어 있는 모든 article을 불러온다
     */
    public List<Article> findAll() {
        List<Article> articleList = blogRepository.findAll();
        for (Article article : articleList) {
            // 삭제된 게시글은 포함하지 않는다
            if (article.getDeletedAt() != null) {
                articleList.remove(article);
            }
        }
        return articleList;
    }

    /*
    클라이언트 인풋에 따라 오름차순 혹은 내림차순으로 블로그를 정렬하며, title이 있는 경우에는 검색하여 유사한 글을 불러온다.
    디폴트는 내림차순 정렬이다.
     */
    public List<ArticleResponse> findArticlesWithParams(String sortingTypeInput, String title) {
       if ((title != null) & (sortingTypeInput != null)) {
           if (sortingTypeInput.contains("ASC")) {
               return getArticlesByTitleSorted(title, false);
           } else {
               // default descending
               return getArticlesByTitleSorted(title, true);
           }
       } else {
           if (sortingTypeInput.contains("ASC")) {
               return getArticlesOrderBy(false).stream().toList();
           } else {
               // default descending
               return getArticlesOrderBy(true).stream().toList();
           }
       }
    }

    /*
    findArticlesWithParams를 위한 메서드로, 생성시간 오름차순/내림차순에 따라 정렬된 블로그 글을 불러온다.
     */
    public List<ArticleResponse> getArticlesOrderBy(boolean isDescending) {
        if (isDescending) {
            return blogRepository.findAllByOrderByCreatedAtDesc().stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        } else {
            return blogRepository.findAllByOrderByCreatedAtAsc().stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        }
    }

    /*
    findArticlesWithParams를 위한 메서드로, title로 검색하여 일부 포함하고 있는 글을 생성시간의 오름차순/내림차순에 따라 정렬하여 불러온다.
     */
    public List<ArticleResponse> getArticlesByTitleSorted(String title, boolean isDescending) {
        if (isDescending) {
            return blogRepository.findAllByTitleContainsOrderByCreatedAtDesc(title).stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        } else {
            return blogRepository.findAllByTitleContainsOrderByCreatedAtAsc(title).stream().filter(i -> i.getDeletedAt() == null).map(ArticleResponse::new).toList();
        }
    }

    /*
    findArticlesWithParams를 리스트가 아닌 Page로 구현한 메서드
     */
    public Page<ArticleResponse> getArticlesPage(int pageNo, String sortingType, String title) {
        Pageable pageable = (sortingType.equals("ASC")) ?
                PageRequest.of(pageNo, 5, Sort.by(Sort.Direction.ASC, "createdAt"))
                : PageRequest.of(pageNo, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        return blogRepository.findAllByTitleContains(title, pageable).map(ArticleResponse::new);
    }


    /*
    JPA의 findById() 메서드를 사용해 ID를 받아 엔티티를 조회하고 없으면 illegalArgumentException 예외 발생
     */
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
    }

    /*
    생성일로부터 10일이 지나면 수정할 수 없기 때문에, 게시글 상세보기하는 경우 수정 가능일을 계산해서 보여준다.
     */
    public UpdateArticleResponse getUpdateMessage(long id) {
        Article article = findById(id);
        // 수정 가능일을 현재날짜 기준으로 계산해서 같이 보여주기
        String message = article.getUpdateMessage();
        return new UpdateArticleResponse(article, message);
    }

    /*
    블로그 글 업데이트 시 생성일 기준으로 10일이 지났다면 수정할 수 없다.
    생성일 기준 9일째에는 하루가 지나면 수정을 못한다는 알림을 처리한다.
     */
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

    /*
    블로그 글을 수정한다.
     */
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

    /*
    soft delete 수행: DB에서는 삭제하지 않지만, 삭제된 시간/업데이트 시간을 기록한다.
     */
    @Transactional
    public Article softDelete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
        article.deleteSoftly();
        return article;
    }

    /*
    hard delete 수행: DB에서 완전히 삭제한다.
     */
    public void hardDelete(long id) {
        blogRepository.deleteById(id);
    }
}
