package com.idol.board.domain.entity;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.UseStatus;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "article")
@Getter
@ToString
// 생성자마다 builder 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.idol.global.common.snowflake.SnowflakeIdGenerator")
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    // TODO :: Member Table과 매핑
    // Todo :: writerId가 있는데 굳이 userId도 필요한가?
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    // TODO :: UserId값 저장 (아이디 이름)
    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "location_address", nullable = false)
    private String locationAddress;

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
    private int minPerson;

    // Integer에는 null 값이 들어갈 위험이 있어서 지향

    @Column(name = "current_person", nullable = false)
    private Integer currentPerson;
    // 숫자 측정 말고 어차피 사람들 포함된 List 객체로 할 것이기 때문에, 해당 객체 삭제하고, 사이즈 값 반환해줌


    @Column(name = "max_person", nullable = false)
    private int maxPerson;

    @Column(name = "article_image_url", nullable = true)
    private String articleImageKey;

    @Column(name = "open_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenStatus openStatus;

    @Column(name = "use_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UseStatus useStatus;


    @Builder
    private Article(Long writerId, String title, Long locationId, String locationAddress, BigCategory bigCategory, SmallCategory smallCategory, String description, Timestamp date, Timestamp deadline, Integer minPerson, Integer maxPerson, String articleImageKey, OpenStatus openStatus, UseStatus useStatus){
        this.writerId = writerId;
        this.title = title;
        this.locationId = locationId;
        this.locationAddress = locationAddress;
        this.bigCategory = bigCategory;
        this.smallCategory = smallCategory;
        this.description = description;
        this.date = date;
        this.deadline = deadline;
        this.minPerson = minPerson;
        this.maxPerson = maxPerson;
        this.articleImageKey = articleImageKey;
        this.openStatus = openStatus;
        this.useStatus = useStatus;
        this.currentPerson = 1;
    }

    public void updateOpenStatus(OpenStatus openStatus) {
        this.openStatus = openStatus;
    }

    public void update(ArticleUpdateRequestDto requestDto) {
        Set<SmallCategory> SPECIAL_CATEGORIES = Set.of(
                SmallCategory.BUSRENTAL_TYPE,
                SmallCategory.COMPANION_TYPE,
                SmallCategory.AFTERPARTY_TYPE
        );

        if (requestDto.title() != null) this.title = requestDto.title();
        if (requestDto.description() != null) this.description = requestDto.description();
        if(requestDto.roadNameAddress() != null) this.locationAddress = requestDto.roadNameAddress();
        if (requestDto.smallCategory() != null) this.smallCategory = requestDto.smallCategory();
        if (requestDto.smallCategory() != null) this.bigCategory = SPECIAL_CATEGORIES.contains(smallCategory) ? BigCategory.GO_TYPE : BigCategory.DOING_TYPE;
        if (requestDto.date() != null) this.date = requestDto.date();
        if (requestDto.deadline() != null) this.deadline = requestDto.deadline();
        if (requestDto.minPerson() != null) this.minPerson = requestDto.minPerson();
        if (requestDto.maxPerson() != null) this.maxPerson = requestDto.maxPerson();
        if (requestDto.imageKey() != null) this.articleImageKey = requestDto.imageKey();
    }

}
