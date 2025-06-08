package com.idol.board.repository.location;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    private final EntityManager entityManager;

    public Optional<Location> findByLocationId(Long locationId){
        String sql = "select a from Location a where a.isDeleted = false and a.locationId = :locationId";
        jakarta.persistence.Query query = entityManager.createQuery(sql)
                .setParameter("locationId", locationId);
        try {
            return Optional.ofNullable((Location) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
