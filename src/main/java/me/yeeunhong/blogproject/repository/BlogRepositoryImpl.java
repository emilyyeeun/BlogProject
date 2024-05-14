package me.yeeunhong.blogproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;

import java.util.List;

import static me.yeeunhong.blogproject.domain.QArticle.article;

@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Article> findByTitle(String title) {
        return queryFactory.selectFrom(article)
                .where(article.title.eq(title))
                .fetch();
    }
}
