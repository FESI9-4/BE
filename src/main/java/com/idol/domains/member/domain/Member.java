package com.idol.domains.member.domain;

import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(
            name = "snowflake-id",
            strategy = "com.idol.global.common.snowflake.SnowflakeIdGenerator"
    )
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img_url", nullable = false)
    private String profileImgUrl;

    public static Member from(SignupMemberRequestDto requestDto, String encryptedPassword) {
        return Member.builder()
                .email(requestDto.email())
                .password(encryptedPassword)
                .nickname(requestDto.nickname())
                .build();
    }

    @Builder
    private Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImgUrl = "";
    }
}
