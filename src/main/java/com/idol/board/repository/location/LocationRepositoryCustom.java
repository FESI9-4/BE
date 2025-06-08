package com.idol.board.repository.location;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;

import java.util.Optional;

public interface LocationRepositoryCustom {
    Optional<Location> findByLocationId(Long locationId);
}
