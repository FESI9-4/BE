package com.idol.board.domain.entity;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.UseStatus;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE article SET is_deleted = true WHERE article_id = ?")  // 삭제 시 상태값 변경
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.idol.global.common.snowflake.SnowflakeIdGenerator")
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    // TODO :: Member Table과 매핑
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    // TODO :: UserId값 저장 (아이디 이름)
    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "big_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private BigCategory bigCategory;

    @Column(name = "small_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private SmallCategory smallCategory;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "deadline", nullable = false)
    private Timestamp deadline;

    @Column(name = "min_person", nullable = false)
    private Integer minPerson;

    @Column(name = "current_person", nullable = false)
    private Integer currentPerson = 1;

    @Column(name = "max_person", nullable = false)
    private Integer maxPerson;

    @Column(name = "article_image_url", nullable = true)
    private String articleImageKey;

    @Column(name = "open_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenStatus openStatus;

    @Column(name = "use_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UseStatus useStatus;

    public void update(ArticleUpdateRequestDto requestDto) {
        if (requestDto.title() != null) this.title = requestDto.title();
        if (requestDto.description() != null) this.description = requestDto.description();
        if (requestDto.smallCategory() != null) this.smallCategory = requestDto.smallCategory();
        if (requestDto.date() != null) this.date = requestDto.date();
        if (requestDto.deadline() != null) this.deadline = requestDto.deadline();
        if (requestDto.minPerson() != null) this.minPerson = requestDto.minPerson();
        if (requestDto.maxPerson() != null) this.maxPerson = requestDto.maxPerson();
        if (requestDto.imageKey() != null) this.articleImageKey = requestDto.imageKey();
    }

}
