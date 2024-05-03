package me.yeeunhong.blogproject.dto;

import lombok.Getter;
import me.yeeunhong.blogproject.domain.Article;

@Getter
public class ArticleResponse implements ArticleResponseInterface {
    private final String title;
    private final String content;
    private final String email;
    private final String phoneNumber;
    private final String author;
    private final String password;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.email = article.getEmail();
        this.phoneNumber = article.getPhoneNumber();
        this.author = article.getAuthor();
        this.password = article.getPassword();
    }
}
