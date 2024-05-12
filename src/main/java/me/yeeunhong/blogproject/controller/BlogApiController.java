package me.yeeunhong.blogproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.DeleteType;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.*;
import me.yeeunhong.blogproject.service.BlogFactory;
import me.yeeunhong.blogproject.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    private final BlogService blogService;
    private final BlogFactory blogFactory;

    /*
    pagination으로 구현한 게시글 가져오기
    생성일 기준으로 title 검색하여 유사한 게시글이 있다면 오름차순/내림차순으로 정렬하여 가져온다.
    list -> page 연습을 위해 두 메서드 분리
     */
    @GetMapping("/api/articles")
    public ResponseEntity<Page<ArticleResponse>> getArticlesListOrPage(@RequestParam(required = false, defaultValue = "pagination", value = "page") String page,
                                                                     @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                                     @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sortingType,
                                                                     @RequestParam(required = false, value = "title") String title) {
        return (page.equals("page")) ? ResponseEntity.ok().body(blogService.getArticlesPage(pageNo, sortingType, title))
                : ResponseEntity.ok().body(new PageImpl<>(blogService.findArticlesWithParams(sortingType, title)));
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

