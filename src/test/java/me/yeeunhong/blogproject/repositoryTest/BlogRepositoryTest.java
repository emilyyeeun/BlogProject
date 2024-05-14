package me.yeeunhong.blogproject.repositoryTest;

import me.yeeunhong.blogproject.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // Repository의 단위 테스트 진행을 위한 Annotation
public class BlogRepositoryTest {
    @Autowired
    // 테스트할 레포지토리는 빈으로 등록되기 때문에, @Autowired을 통해 의존성 주입
    private BlogRepository blogRepository;

}