package com.idol.domains.wish.domain;

import com.idol.domains.member.domain.Member;
import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "wish")
public class Wish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id", nullable = false)
    public Long wishId;

    @Column(name = "member_id", nullable = false)
    public Long memberId;

    @Column(name = "article_id", nullable = false)
    public  Long articleId;

    public static Wish from(Long memberId, Long articleId) {
        return Wish.builder()
                .memberId(memberId)
                .articleId(articleId)
                .build();
    }

    @Builder
    private Wish(Long memberId, Long articleId) {
        this.memberId = memberId;
        this.articleId = articleId;
    }
}
