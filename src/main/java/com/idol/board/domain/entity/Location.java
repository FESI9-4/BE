package com.idol.board.domain.entity;

import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import com.idol.global.common.snowflake.Snowflake;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Table(name = "location")
@Getter
@ToString
@NoArgsConstructor
public class Location extends BaseEntity {

    // 정적 Snowflake 인스턴스
    private static final Snowflake snowflake = new Snowflake();

    @PrePersist
    public void generateId() {
        if (this.locationId == null) {
            this.locationId = snowflake.nextId();
        }
    }

    @Id
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "latitude", nullable = false)
    private double latitude;
    @Column(name = "longitude", nullable = false)
    private double longitude;
    @Column(name = "road_name_address", nullable = false)
    private String roadNameAddress;

    @Builder
    public Location(double latitude, double longitude, String roadNameAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadNameAddress = roadNameAddress;
    }

    public void update(ArticleUpdateRequestDto requestDto) {
        if (requestDto.roadNameAddress() != null) this.roadNameAddress = requestDto.roadNameAddress();
        if (requestDto.latitude() != null) this.latitude = requestDto.latitude();
        if (requestDto.longitude() != null) this.longitude = requestDto.longitude();
    }
}
