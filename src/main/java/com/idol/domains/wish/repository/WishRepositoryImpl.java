package com.idol.domains.wish.repository;

import com.idol.domains.wish.domain.QWish;
import com.idol.domains.wish.domain.Wish;
import com.idol.domains.wish.dto.response.GetMembersWishListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class WishRepositoryImpl implements WishRepository {

    private final WishJpaRepository wishJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Wish save(Wish wish) {
        return wishJpaRepository.save(wish);
    }

    @Override
    public void saveAll(List<Wish> wishes) {
        wishJpaRepository.saveAll(wishes);
    }

    @Override
    public void deleteByMemberIdAndArticleId(Long memberId, Long articleId) {
        wishJpaRepository.deleteByMemberIdAndArticleId(memberId, articleId);
    }

    @Override
    public List<GetMembersWishListResponseDto> findByMemberId(Long memberId) {
        QWish wish = QWish.wish;

        return queryFactory
                .select(Projections.constructor(GetMembersWishListResponseDto.class,
                        wish.wishId,
                        wish.articleId))
                .from(wish)
                .where(wish.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public List<Long> findArticleIdsByMemberId(Long memberId) {
        return wishJpaRepository.findArticleIdsByMemberId(memberId);
    }

    @Override
    public Long countByMemberIdAndIsDeletedFalse(Long memberId) {
        return wishJpaRepository.countByMemberIdAndIsDeletedFalse(memberId);
    }
}
