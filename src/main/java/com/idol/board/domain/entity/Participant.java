package com.idol.board.domain.entity;

<<<<<<< HEAD
import com.idol.board.domain.UseStatus;
import com.idol.global.common.entity.BaseEntity;
import com.idol.global.common.snowflake.Snowflake;
import jakarta.persistence.*;
import lombok.*;
=======
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
>>>>>>> d88981d (참여자 관련 코드 구현)
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "participant")
@Getter
@ToString
// 생성자마다 builder 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
public class Participant extends BaseEntity {
    // 정적 Snowflake 인스턴스
    private static final Snowflake snowflake = new Snowflake();

    @PrePersist
    public void generateId() {
        if (this.participantId == null) {
            this.participantId = snowflake.nextId();
        }
    }

    @Id
=======
public class Participant {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.idol.global.common.snowflake.SnowflakeIdGenerator")
>>>>>>> d88981d (참여자 관련 코드 구현)
    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @Column(name = "nickname", nullable = false)
    private String nickname;
<<<<<<< HEAD

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Builder
    public Participant(Long articleId, String participantImageKey, String participantNickname, Long writerId) {
        this.articleId = articleId;
        this.imageKey = participantImageKey;
        this.nickname = participantNickname;
        this.writerId = writerId;
    }
=======
>>>>>>> d88981d (참여자 관련 코드 구현)
}
