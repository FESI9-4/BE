package com.idol.domains.wish.service;

import com.idol.domains.wish.repository.WishRepository;
import com.idol.domains.wish.usecase.CountWishUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CountWishService implements CountWishUsecase {

    private final WishRepository wishRepository;

    @Override
    public Long countWishesByMemberId(Long memberId) {
        return wishRepository.countByMemberIdAndIsDeletedFalse(memberId);
    }
}
