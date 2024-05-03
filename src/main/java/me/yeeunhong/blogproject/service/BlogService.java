package me.yeeunhong.blogproject.service;

import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.AddArticleRequest;
import me.yeeunhong.blogproject.dto.UpdateArticleRequest;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    // 블로그 추가하기
    public Article save(AddArticleRequest request) {
        checkTitle(request.getTitle());
        checkContent(request.getContent());
        checkEmail(request.getEmail());
        checkPhoneNum(request.getPhoneNumber());
        checkPassword(request.getPassword());
        return blogRepository.save(request.toEntity());
    }

    private void checkTitle(String title) {
        // 1. title length 확인
        if (title.length() >= 200) {
            // title 이 글자 수 200 이상이 되면 안됨
            throw new IllegalArgumentException("title is longer than 200 characters");
        }
    }

    private void checkContent(String content) {
        // 2. content length 확인
        if (content.length() >= 1000) {
            // content 가 1000자 이상이 되어서는 안됨
            throw new IllegalArgumentException("content is longer than 1000 characters");
        }
    }

    private void checkEmail(String email) {
        final String emailFormat = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        //
        // 3. 이메일 형식 확인
        if (!isCorrectRegex(email, emailFormat)) {
            throw new IllegalArgumentException("wrong email format");
        }
    }

    private void checkPhoneNum(String phoneNumber) {
        final String phoneFormat = "\\d{3}-?\\d{4}-?\\d{4}";
        // 4. 휴대폰번호 형식 확인
        if (!isCorrectRegex(phoneNumber, phoneFormat)) {
            throw new IllegalArgumentException("wrong phone number format");
        }
    }

    private void checkPassword(String password) {
        // 5. 비밀번호 형식 확인
        // 대소문자로 이뤄져야하고 숫자가 5개이상 특수문자가 *!@#$%을 포함을 2개이상 해야한다.
        final String passwordFormat = "([a-zA-Z]+)([0-9]{5,})([*!@#$%]{2,})";
        if (!isCorrectRegex(password, passwordFormat)) {
            throw new IllegalArgumentException("wrong password format");
        }
    }

    // 정규식 체크하는 helper 함수
    private boolean isCorrectRegex(String input, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

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

    // JPA의 findById() 메서드를 사용해 ID를 받아 엔티티를 조회하고 없으면 illegalArgumentException 예외 발생
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
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

    public long numDaysFromUpdated(Article article) {
        // 작성일자가 10일이 넘었는지 확인
        return Duration.between(article.getCreatedAt(), LocalDateTime.now()).toDays();
    }

    public String getUpdateMessage(Article article) {
        // 수정 가능일을 현재날짜 기준으로 계산해서 같이 보여주기
        LocalDateTime updatableDays = article.getCreatedAt().plusDays(9);
        String message = "오늘 날짜는 " + LocalDateTime.now() + "입니다."
                + "수정 가능일은 " + updatableDays + "까지입니다.";
        return message;
    }

    @Transactional // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    // 엔티티의 필드 값이 바뀌면 중간에 에러가 발생해도 제대로 된 값 수정을 보장
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found " + id));

        article.update(request.getTitle(), request.getContent(), request.getEmail(), request.getPhoneNum()
                , request.getAuthor(), request.getPassword());
        return article;
    }
}
