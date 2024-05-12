package me.yeeunhong.blogproject.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yeeunhong.blogproject.DeleteType;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.AddArticleRequest;
import me.yeeunhong.blogproject.dto.UpdateArticleRequest;
import me.yeeunhong.blogproject.repository.BlogRepository;
import me.yeeunhong.blogproject.service.BlogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.transform.Result;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
Controller Test: 통합 테스트 코드 작성
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogFactory blogFactory;
    @Autowired
    private WebApplicationContext context;
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    private Article createTestArticle(String title, String content, String email, String phoneNum, String author, String password) {
        return blogFactory.save(new AddArticleRequest(title, content, email, phoneNum, author, password));
    }

    @DisplayName("getArticles: 조건에 맞는 블로그 글 가져오기에 성공한다.")
    @Test
    public void getArticles() throws Exception {
        String url = "/api/articles/";
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";

        Article article = createTestArticle(title, content, email, phoneNum, author, password);

        final String title2 = "title2";
        final String content2 = "content2";
        final String email2 = "email2@gmail.com";
        final String phoneNum2 = "010-3333-3333";
        final String author2 = "author2";
        final String password2 = "PassWord12345@@";

        Article article2 = createTestArticle(title, content, email, phoneNum, author, password);

        ResultActions resultActions = mockMvc.perform(get("/api/articles")
                        .param("page", "page")
                        .param("pageNo", "0")
                        .param("sort", "DESC")
                        .param("title", "title"))
                .andExpect(status().isOk());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(2));

    }

    /*
    get article test
     */
    @DisplayName("getArticle: 블로그 글 가져오기에 성공한다.")
    @Test
    public void getArticle() throws Exception {
        String url = "/api/articles/";
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";

        Article article = createTestArticle(title, content, email, phoneNum, author, password);
        url = url + article.getId();

        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    /*
    get details test
     */
    @DisplayName("getDetails: 게시글 상세보기에 성공한다.")
    @Test
    public void getDetails() throws Exception {
        String url = "/api/articles/details/";
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";

        Article article = createTestArticle(title, content, email, phoneNum, author, password);
        url = url + article.getId();

        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("오늘 날짜는 2024-05-12입니다. 수정 가능일은 2024-05-21까지입니다."));
    }

    /*
    post article test
     */
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

    /*
    put article test
     */
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
        DeleteType deleteType = DeleteType.HARD_DELETE;

        mockMvc.perform(delete("/api/articles/" + savedArticle.getId())
                .param("deleteType", "HARD_DELETE"))
                .andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("deleteArticle: 블로그 글 목록 삭제에 성공한다. (soft delete)")
    @Test
    public void deleteSoftly() throws Exception {
        final String title = "title";
        final String content = "content";
        final String email = "email@gmail.com";
        final String phoneNum = "010-2222-3333";
        final String author = "author";
        final String password = "PassWord12345@!";
        Article savedArticle = createTestArticle(title, content, email, phoneNum, author, password);
        DeleteType deleteType = DeleteType.HARD_DELETE;

        mockMvc.perform(delete("/api/articles/" + savedArticle.getId())
                        .param("deleteType", "SOFT_DELETE"))
                .andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
    }

}
