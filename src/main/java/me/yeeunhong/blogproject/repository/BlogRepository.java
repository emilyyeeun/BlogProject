package me.yeeunhong.blogproject.repository;

import me.yeeunhong.blogproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

// 엔티티 Article과 엔티티의 PK 타입 Long
public interface BlogRepository extends JpaRepository <Article, Long> {

    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByCreatedAtAsc();

    List<Article> findAllByTitleContainsOrderByCreatedAtDesc(String title);
    List<Article> findAllByTitleContainsOrderByCreatedAtAsc(String title);

    Page<Article> findAllByTitleContains(String title, Pageable pageable);
}
