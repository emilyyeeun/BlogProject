package me.yeeunhong.blogproject.domain;

/*
데이터의 생성시간, 수정시간, 생성한 사람, 마지막으로 수정한 사람 등을 저장해야할 때
사용자가 직접 생성해내는 데이터들은 중복으로 저장이 필요하게 된다.
모든 엔티티에 매번 컬럼으로 지정하여 코드를 작성하는 것은 번거롭기 때문에 자동화하고 Entity로 빼내어 필요한 엔티티들은 상속받도록 한다.
 */

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp // 엔티티가 생성될 때 생성 시간 저장
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp // 엔티티가 수정될 때 수정 시간 저장
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    // 엔티티가 삭제된다면 삭제 시간 저장
    private LocalDateTime deletedAt;

    public void deleteSoftly() {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = deletedAt;
    }
}
