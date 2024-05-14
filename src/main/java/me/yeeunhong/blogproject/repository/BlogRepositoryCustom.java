package me.yeeunhong.blogproject.repository;

import me.yeeunhong.blogproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogRepositoryCustom {
    Page<Article> findAllByTitleContains(String title, Pageable pageable);
}
