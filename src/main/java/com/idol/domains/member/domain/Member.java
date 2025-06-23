package com.idol.domains.member.domain;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import com.idol.board.service.myPage.command.UpdateMyPageService;
import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.global.common.entity.BaseEntity;
import com.idol.global.common.snowflake.Snowflake;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img_url", nullable = false)
    private String profileImgUrl;


    @Column(name = "information", nullable = true)
    private String information;


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


    public void update(MyPageUpdateRequestDto dto) {
        if(dto.email()  != null ) this.email = dto.email();
        if(dto.nickName() != null) this.nickname = dto.nickName();
        if(dto.information() != null) this.information = dto.information();
        if(dto.profileImgUrl() != null) this.profileImgUrl = dto.profileImgUrl();
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
