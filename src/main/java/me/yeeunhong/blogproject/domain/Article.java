package me.yeeunhong.blogproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="ARTICLE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {
    @Id // 기본 키 지정
    // 하나씩 increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // title is not null
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    // 작성자를 제외한 항목들은 optional
    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "phone", nullable = true)
    private String phoneNumber;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "password", nullable = true)
    private String password;

    // 빌더 패턴 방식으로 객체를 생성할 수 있어 편리하다 (롬복)
    @Builder
    public Article(String title, String content, String email, String phoneNumber, String author, String password) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.author = author;
        this.password = password;
    }

    public void update(String title, String content, String email, String phoneNumber, String author, String password) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.author = author;
        this.password = password;
    }



//    // NoArgsConsturctor로 대체
//    protected Article() {
//        // 기본 생성자
//    }

//    // getter 롬복 애너테이션으로 대체
//    public Long getId() {
//        return id;
//    }
//    public String getTitle() {
//        return title;
//    }
//    public String getContent() {
//        return content;
//    }
}
