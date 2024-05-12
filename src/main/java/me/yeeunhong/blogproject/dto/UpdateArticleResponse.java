package me.yeeunhong.blogproject.dto;

import lombok.Getter;
import me.yeeunhong.blogproject.domain.Article;

@Getter
public class UpdateArticleResponse implements ArticleResponseInterface {
    private final String message;
    private final String title;
    private final String content;
    private final String email;
    private final String phoneNumber;
    private final String author;
    private final String password;


    public UpdateArticleResponse(Article article, String message) {
            this.title = article.getTitle();
            this.content = article.getContent();
            this.email = article.getEmail();
            this.phoneNumber = article.getPhoneNumber();
            this.author = article.getAuthor();
            this.password = article.getPassword();
            // ArticleResponse와 달리 UpdateArticleResponse는 message를 함께 출력한다.
            this.message = message;
    }
}
