package com.idol.domains.wish.service;

import com.idol.domains.wish.repository.WishRepository;
import com.idol.domains.wish.usecase.GetWishListByMemberIdUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GetWishListByMemberIdService implements GetWishListByMemberIdUsecase {

    private final WishRepository wishRepository;

    @Override
    public List<Long> findArticleIdsByMemberId(Long memberId) {
        return wishRepository.findArticleIdsByMemberId(memberId);
    }
}
