package controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yeeunhong.blogproject.BlogProjectApplication;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.AddArticleRequest;
import me.yeeunhong.blogproject.dto.UpdateArticleRequest;
import me.yeeunhong.blogproject.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setUp() {
        blogRepository.deleteAll();
    }

    private Article createTestArticle(String title, String content, String email, String phoneNum, String author, String password) {
        return blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .email(email).phoneNumber(phoneNum).author(author).password(password)
                .build());
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content, email, phoneNum, author, password);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("deleteArticle: 블로그 글 목록 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";
        Article savedArticle = createTestArticle(title, content, email, phoneNum, author, password);

        final String url = "/api/articles/" + savedArticle.getId();

        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception {
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";
        Article savedArticle = createTestArticle(title, content, email, phoneNum, author, password);

        final String newTitle = "new title";
        final String newContent = "new content";
        final String newEmail = "emailNew@gmail.com";
        final String newPhoneNum = "010-2222-3456";
        final String newAuthor = "author New";
        final String newPassword = "PassWord12345@!";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent, newEmail, newPhoneNum
        , newAuthor, newPassword);

        final String url = "/api/articles/" + savedArticle.getId();

        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
