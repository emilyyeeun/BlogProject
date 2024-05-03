package me.yeeunhong.blogproject.dto;

import lombok.Getter;
import me.yeeunhong.blogproject.domain.Article;

public class UpdateArticleResponse implements ArticleResponseInterface {
    private String message;
    public UpdateArticleResponse(Article article) {
            this.message = "하루가 지나면 수정이 불가합니다.";
    }
}
