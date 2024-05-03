package me.yeeunhong.blogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {

    private String title;
    private String content;
    private String email;
    private String phoneNum;
    private String author;
    private String password;
}
