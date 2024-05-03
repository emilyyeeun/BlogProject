package me.yeeunhong.blogproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.DeleteType;
import me.yeeunhong.blogproject.domain.Article;
import me.yeeunhong.blogproject.dto.*;
import me.yeeunhong.blogproject.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    private final BlogService blogService;

    // controller의 정렬은 get -> post -> put -> delete 순
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        // Get 요청이 오면 글 전체를 조회하는 findAll() 메서드를 호출한 다음 응답용 객체인 articleReponse로 파싱해 객체에 담아 클라이언트에게 전송
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();
        // ResponseEntity를 쓰는 경우 1) 규칙성 2) HTTP status에 대해 바로바로 사용이 가능하다
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // 게시글 상세보기
    @GetMapping("/api/articles/details/{id}")
    public ResponseEntity<UpdatableDateResponse> getArticleDetails(@PathVariable long id) {
        Article article = blogService.findById(id);
        String message = blogService.getUpdateMessage(article);
        // 수정 가능일을 현재날짜 기준으로 계산해서 같이 보여주기
        return ResponseEntity.ok().body(new UpdatableDateResponse(message));
    }

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    // @Valid를 통해 입력 파라미터의 유효성 검증
    public ResponseEntity<ArticleResponse> addArticle(@Valid @RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        // 요청한 자원이 성공정으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송 (201)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponseInterface> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article articleToUpdate = blogService.findById(id);
        long daysToUpdate = blogService.numDaysFromUpdated(articleToUpdate);
        if (daysToUpdate < 9) {
            Article updatedArticle = blogService.update(id, request);
            return ResponseEntity.ok()
                    .body(new ArticleResponse(updatedArticle));
        } else if (daysToUpdate == 9) {
            return ResponseEntity.ok()
                    .body(new UpdateArticleResponse(articleToUpdate));
        } else {
            return ResponseEntity.ok().body(new ArticleResponse(articleToUpdate));
        }
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

