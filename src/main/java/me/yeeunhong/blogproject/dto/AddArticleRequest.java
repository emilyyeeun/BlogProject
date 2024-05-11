package me.yeeunhong.blogproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;

/*
DTO; 계층끼리 데이터를 교환하기 위해 사용하는 객체로, 단순하게 데이터를 옮기기 때문에 비즈니스 로직을 포함하지 않는다.
 */
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @Email
    private String email;
    private String phoneNumber;
    @NotBlank(message = "author cannot be blank")
    private String author;
    @NotBlank(message = "password cannot be blank")
    private String password;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .email(email)
                .author(author)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }
}
