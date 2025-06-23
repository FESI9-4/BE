package com.idol.domains.wish.repository;

import com.idol.domains.wish.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishJpaRepository extends JpaRepository<Wish, Long> {
    List<Long> findArticleIdsByMemberId(Long memberId);
    Long countByMemberIdAndIsDeletedFalse(Long memberId);

    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);


}
