package com.idol.domains.wish.service;

import com.idol.domains.wish.repository.WishRepository;
import com.idol.domains.wish.usecase.WishUnlikeUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class WishUnlikeService implements WishUnlikeUsecase {

    private final WishRepository wishRepository;

    @Override
    public void WishUnlike(Long articleId, Long memberId) {
        wishRepository.deleteByMemberIdAndArticleId(memberId, articleId);
    }
}
