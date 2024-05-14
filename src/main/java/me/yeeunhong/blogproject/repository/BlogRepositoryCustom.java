package me.yeeunhong.blogproject.repository;

import me.yeeunhong.blogproject.domain.Article;

import java.util.List;

public interface BlogRepositoryCustom {
    List<Article> findByTitle(String title);
}
