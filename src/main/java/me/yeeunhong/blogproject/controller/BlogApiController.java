package me.yeeunhong.blogproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.DeleteType;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.*;
import me.yeeunhong.blogproject.service.BlogFactory;
import me.yeeunhong.blogproject.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    private final BlogService blogService;
    private final BlogFactory blogFactory;

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(@RequestParam(required = false, defaultValue = "createdAt", value = "orderby") String sortingTypeInput,
                                                                 @RequestParam(required = false, value = "title") String title) {
        return ResponseEntity.ok().body(blogService.findArticlesWithParams(sortingTypeInput, title).stream().toList());
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // 게시글 상세보기
    @GetMapping("/api/articles/details/{id}")
    public ResponseEntity<UpdateArticleResponse> getArticleDetails(@PathVariable long id) {
        UpdateArticleResponse updateArticleResponse = blogService.getUpdateMessage(id);
        // 수정 가능일을 현재날짜 기준으로 계산해서 같이 보여주기
        return ResponseEntity.ok()
                .body(updateArticleResponse);
    }



    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    // @Valid를 통해 입력 파라미터의 유효성 검증
    public ResponseEntity<ArticleResponse> addArticle(@Valid @RequestBody AddArticleRequest request) {
        Article savedArticle = blogFactory.save(request);
        // 요청한 자원이 성공정으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송 (201)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponseInterface> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        ArticleResponseInterface updateArticle = blogService.updateArticleWithValidation(id, request);
        return ResponseEntity.ok().body(updateArticle);
    }

    // soft delete & hard delete
    @DeleteMapping("/api/articles/{deleteType}/{id}")
    public ResponseEntity<ArticleResponse> deleteArticle(@RequestParam("deleteType") DeleteType deleteType,
                                                         @RequestParam("id") long id) {
        if (deleteType == DeleteType.SOFT_DELETE) {
            Article article = blogService.softDelete(id);
            return ResponseEntity.ok().body(new ArticleResponse(article));
        } else {
            blogService.hardDelete(id);
            return ResponseEntity.ok().build();
        }
    }
}

