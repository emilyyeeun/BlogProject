package me.yeeunhong.blogproject.repositoryTest;

import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest // Repository의 단위 테스트 진행을 위한 Annotation
//public class BlogRepositoryTest {
//    @Autowired
//    // 테스트할 레포지토리는 빈으로 등록되기 때문에, @Autowired을 통해 의존성 주입
//    private BlogRepository blogRepository;
//
//    @Test
//    @DisplayName("article이 제대로 저장되는지 확인")
//    void addArticle() {
//        //given
//        Article article = new Article("Test Title", "Test Content");
//
//        //when
//        Article savedArticle = blogRepository.save(article);
//
//        //then
//        assertThat(savedArticle.getTitle()).isEqualTo(article.getTitle());
//    }
//
//    @Test
//    @DisplayName("저장되어 있는 모든 article 조회가 가능한지 확인")
//    void findArticles() {
//        //given
//        Article articleOne = new Article("Test Title 1", "Test Content 1");
//        blogRepository.save(articleOne);
//
//        //when
//        List<Article> foundArticles = blogRepository.findAll();
//
//        //then
//        assertThat(foundArticles.get(foundArticles.size() - 1).getTitle()).isEqualTo(articleOne.getTitle());
//    }
//
//    @Test
//    @DisplayName("id별로 article 조회가 가능한지 확인")
//    void findByIdArticles() {
//        //given
//        Article articleOne = new Article("Test Title 1", "Test Content 1");
//        blogRepository.save(articleOne);
//
//        //when
//        Optional<Article> newArticle = blogRepository.findById(4L);
//
//        //then
//        assertThat(newArticle.get().getTitle()).isEqualTo(articleOne.getTitle());
//    }
//
//    @Test
//    @DisplayName("id별로 article 삭제 가능한지 확인")
//    void deleteByIdArticles() {
//        //given
//        Article articleOne = new Article("Test Title 1", "Test Content 1");
//        blogRepository.save(articleOne);
//
//        //when
//        blogRepository.deleteById(4L);
//
//        //then
//        assertThat(articleOne).isNull();
//    }
//
//}