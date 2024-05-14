package me.yeeunhong.blogproject.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.yeeunhong.blogproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static me.yeeunhong.blogproject.domain.QArticle.article;

@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Article> findAllByTitleContains(String title, Pageable pageable) {
        QueryResults<Article> queryResults = queryFactory
                .selectFrom(article)
                .where(article.title.like("%" + title + "%"))
                .orderBy(article.createdAt.desc()) // Order by createdAt in descending order
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
