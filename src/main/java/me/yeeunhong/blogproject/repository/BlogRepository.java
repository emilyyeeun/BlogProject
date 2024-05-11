package me.yeeunhong.blogproject.repository;

import me.yeeunhong.blogproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

// 엔티티 Article과 엔티티의 PK 타입 Long
public interface BlogRepository extends JpaRepository <Article, Long> {
    // JPA query method 사용: createdAt을 기준으로 내림차순/오름차순 기준으로 리스팅
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByCreatedAtAsc();

    List<Article> findAllByTitleContainsOrderByCreatedAtDesc(String title);
    List<Article> findAllByTitleContainsOrderByCreatedAtAsc(String title);
}
