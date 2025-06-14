package com.idol.board.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "participant")
@Getter
@ToString
// 생성자마다 builder 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.idol.global.common.snowflake.SnowflakeIdGenerator")
    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @Column(name = "nickname", nullable = false)
    private String nickname;
}
