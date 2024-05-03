package serviceTest;

import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.AddArticleRequest;
import me.yeeunhong.blogproject.dto.UpdateArticleRequest;
import me.yeeunhong.blogproject.repository.BlogRepository;
import me.yeeunhong.blogproject.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    private BlogRepository blogRepository;

    // 의존성 주입
    @InjectMocks
    private BlogService blogService;

    @BeforeEach
    void setUp() {
        // 초기화 코드 삽입
        blogRepository.deleteAll();
    }

    @DisplayName(("addArticle: 새로운 글을 추가한다."))
    @Test
    void addArticleTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("Test Title", "Test Content",
                "testemail123@gmail.com", "010-1111-2222", "test author",
                "AbCd12345@!");
        Article article = request.toEntity();

        // stub (repository를 mock으로 만들어 주입하였기 때문에 mock의 행위를 정의하는 과정)
        // 어떤 객체가 저장되든 테스트 케이스에서 생성한 객체를 리턴
        when(blogRepository.save(any(Article.class))).thenReturn(article);

        // when (함수 실제 호출)
        Article savedArticle = blogService.save(request);

        // then (동작 검증)
        assertThat(savedArticle).isNotNull();
        assertThat(savedArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(savedArticle.getContent()).isEqualTo(article.getContent());
        assertThat(savedArticle.getEmail()).isEqualTo(article.getEmail());
        assertThat(savedArticle.getPhoneNumber()).isEqualTo(article.getPhoneNumber());
        assertThat(savedArticle.getAuthor()).isEqualTo(article.getAuthor());
        assertThat(savedArticle.getPassword()).isEqualTo(article.getPassword());
        verify(blogRepository).save(any());
    }

    @DisplayName(("addArticleFailTitleTest: 새로운 글 추가 시 Fail - title"))
    @Test
    void addArticleFailTitleTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("looooooooooooooooooooooooooooooooooooooooooooooong " +
                "looooooooooooooooooooooooooooooooooooooooooooooong" +
                "looooooooooooooooooooooooooooooooooooooooooooooong" +
                "looooooooooooooooooooooooooooooooooooooooooooooong" +
                "looooooooooooooooooooooooooooooooooooooooooooooong" +
                "title",
                "short content",
                "test123@gmail.com", "010-1111-2222", "test author",
                "AbCd12345@!");


        // then (동작 검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogService.save(request));
        assertEquals("title is longer than 200 characters", exception.getMessage());
    }

    @DisplayName(("addArticleFailContentTest: 새로운 글 추가 시 Fail - content"))
    @Test
    void addArticleFailContentTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("title",
                "hellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooohellooooooooooo",
                "test123@gmail.com", "010-1111-2222", "test author",
                "AbCd12345@!");


        // then (동작 검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogService.save(request));
        assertEquals("content is longer than 1000 characters", exception.getMessage());
    }

    @DisplayName(("addArticleFailEmailTest: 새로운 글 추가 시 Fail - email"))
    @Test
    void addArticleFailEmailTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("title",
                "hello", "wrongemail", "010-1111-2222", "test author",
                "AbCd12345@!");


        // then (동작 검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogService.save(request));
        assertEquals("wrong email format", exception.getMessage());
    }

    @DisplayName(("addArticleFailPhoneNumTest: 새로운 글 추가 시 Fail - phoneNumber"))
    @Test
    void addArticleFailPhoneNumTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("title",
                "hello", "email123@gmail.com", "010", "test author",
                "AbCd12345@!");


        // then (동작 검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogService.save(request));
        assertEquals("wrong phone number format", exception.getMessage());
    }

    @DisplayName(("addArticlePasswordTest: 새로운 글 추가 시 Fail - password"))
    @Test
    void addArticleFailPasswordTest() {
        // given
        AddArticleRequest request = new AddArticleRequest("title",
                "hello", "email123@gmail.com", "010-2121-3232", "",
                "dd");
        Article article = request.toEntity();

        // then (동작 검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogService.save(request));
        assertEquals("wrong password format", exception.getMessage());
    }


    @DisplayName(("findArticles: 모든 글을 조회한다."))
    @Test
    void findAllArticlesTest() {
        // given
        List<Article> articleList = new LinkedList<>();
        articleList.add(new Article("Test Title1", "Test Content1",
                "testemail123@gmail.com", "010-1111-2222", "test author1", "AbCd12345@!"));
        articleList.add(new Article("Test Title2", "Test Content2",
                "testemail456@gmail.com", "010-4444-5555", "test author2", "AbCd12345@!"));
        articleList.add(new Article("Test Title3", "Test Content3",
                "testemail789@gmail.com", "010-5555-6666", "test author3", "AbCd12345@!"));

        List<Article> savedList = articleList;

        // stub
        when(blogRepository.findAll()).thenReturn(savedList);

        // when
        List<Article> resultList = blogService.findAll();

        // then
        assertThat(resultList).hasSize(3);
        assertThat(resultList.get(0).getTitle()).isEqualTo(savedList.get(0).getTitle());
        assertThat(resultList.get(0).getContent()).isEqualTo(savedList.get(0).getContent());
        assertThat(resultList.get(0).getEmail()).isEqualTo(savedList.get(0).getEmail());
        verify(blogRepository).findAll();

    }

    @DisplayName(("find By Id: id로 글 조회"))
    @Test
    void findByIdTest() {
        // given
        Long articleId = 1L;
        Article savedArticle = new Article("Test Title1", "Test Content1",
                "testemail123@gmail.com", "010-1111-2222", "test author", "AbCd12345@!");
        blogRepository.save(savedArticle);


        // stub
        when(blogRepository.findById(articleId)).thenReturn(Optional.of(savedArticle));

        // when
        Article foundArticle = blogService.findById(articleId);

        // then
        assertThat(foundArticle).isNotNull();
        assertThat(foundArticle.getTitle()).isEqualTo(savedArticle.getTitle());
        assertThat(foundArticle.getContent()).isEqualTo(savedArticle.getContent());
        verify(blogRepository).findById(articleId);
    }

    @DisplayName(("find By Id Exception Test: findbyId 에러"))
    @Test
    void findByIdExceptionTest() {
        // given
        Long id = 100L;

        // stub
        when(blogRepository.findById(id)).thenThrow(IllegalArgumentException.class);

        // when/then
        assertThrows(IllegalArgumentException.class, () -> blogService.findById(id));
    }

    @DisplayName(("delete: 블로그 글 삭제"))
    @Test
    void hardDeleteTest() {
        // given
        Long articleId = 1L;
        Article article = new Article("Test Title1", "Test Content1",
                "testemail123@gmail.com", "010-1111-2222", "test author", "AbCd12345@!");
        blogRepository.save(article);

        doNothing().when(blogRepository).deleteById(articleId);

        // when (함수 실제 호출)
        blogService.hardDelete(articleId);

        // then
        List<Article> articleList = blogRepository.findAll();
        assertThat(articleList).isEmpty();

        verify(blogRepository, times(1)).deleteById(articleId);
    }

    @DisplayName(("delete: 블로그 글 삭제 - softDelete"))
    @Test
    void softDeleteTest() {
        // given
        Long articleId = 1L;
        Article savedArticle = new Article("Test Title1", "Test Content1",
                "testemail123@gmail.com", "010-1111-2222", "test author", "AbCd12345@!");
        blogRepository.save(savedArticle);


        // stub
        when(blogRepository.findById(articleId)).thenReturn(Optional.of(savedArticle));

        // when
        Article deletedArticle = blogService.softDelete(articleId);

        System.out.println(deletedArticle.getDeletedAt());
        // then
        assertThat(deletedArticle.getDeletedAt()).isNotNull();
        assertThat(deletedArticle.getDeletedAt()).isEqualTo(savedArticle.getUpdatedAt());
        assertThat(deletedArticle.getTitle()).isEqualTo(savedArticle.getTitle());
    }

    @DisplayName(("update: 블로그 글 수정"))
    @Test
    void updateTest() {
        // given
        Long articleId = 1L;
        Article article = new Article("Test Title1", "Test Content1",
                "testemail123@gmail.com", "010-1111-2222", "test author", "AbCd12345@!");
        blogRepository.save(article);

        UpdateArticleRequest request = new UpdateArticleRequest("Updated Title", "Updated Content",
                "updatedemail123@gmail.com", "010-2222-3333", "updated author", "UpDated12345@!");
        when(blogRepository.findById(articleId)).thenReturn(Optional.of(article));
        article.update(request.getTitle(), request.getContent(), request.getEmail(), request.getPhoneNum(),
                request.getAuthor(), request.getPassword());

        // when
        Article updatedArticle = blogService.update(articleId, request);

        // then
        assertThat(article.getTitle()).isEqualTo(updatedArticle.getTitle());
        assertThat(article.getContent()).isEqualTo(updatedArticle.getContent());
        assertThat(article.getEmail()).isEqualTo(updatedArticle.getEmail());
        assertThat(article.getPhoneNumber()).isEqualTo(updatedArticle.getPhoneNumber());
        assertThat(article.getAuthor()).isEqualTo(updatedArticle.getAuthor());
        assertThat(article.getPassword()).isEqualTo(updatedArticle.getPassword());
    }

    @DisplayName(("Update Exception Test: 업데이트 에러"))
    @Test
    void UpdateExceptionTest() {
        // given
        UpdateArticleRequest request = new UpdateArticleRequest("Test Title", "Test Content",
                "testemail123@gmail.com", "010-1111-2222", "test author", "AbCd12345@!");
        Long id = 100L;
        // stub
        when(blogRepository.findById(id)).thenThrow(IllegalArgumentException.class);

        // when/then
        assertThrows(IllegalArgumentException.class, () -> blogService.update(id, request));
    }

}
