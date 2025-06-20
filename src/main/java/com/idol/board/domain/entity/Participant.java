package com.idol.board.domain.entity;


import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "participant")
@Getter
@ToString
// 생성자마다 builder 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseEntity {
    @Id
    @Column(name = "participant_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Builder
    public Participant(Long articleId, String participantImageKey, String participantNickname, Long writerId) {
        this.articleId = articleId;
        this.imageKey = participantImageKey;
        this.nickname = participantNickname;
        this.writerId = writerId;
    }

}
