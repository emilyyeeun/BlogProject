package me.yeeunhong.blogproject.repository;

import me.yeeunhong.blogproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// 엔티티 Article과 엔티티의 PK 타입 Long
public interface BlogRepository extends JpaRepository <Article, Long> {

    // Containing을 붙여주면 Like 검색이 가능해짐
    Page<Article> findByTitleContaining(String keyword, Pageable pageable);
}
