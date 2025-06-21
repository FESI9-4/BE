package com.idol.domains.wish.service;

import com.idol.domains.wish.domain.Wish;
import com.idol.domains.wish.dto.request.WishLikeRequestDto;
import com.idol.domains.wish.repository.WishRepository;
import com.idol.domains.wish.usecase.WishlikeUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class WishLikeService implements WishlikeUsecase {

    private final WishRepository wishRepository;

    @Override
    public void WishLike(WishLikeRequestDto requestDto, Long memberId) {
        List<Long> existingArticleIds = wishRepository.findArticleIdsByMemberId(memberId);

        List<Wish> newWishes = convertToWishList(requestDto, memberId, existingArticleIds);

        if (!newWishes.isEmpty()) {
            wishRepository.saveAll(newWishes);
        }
    }

    private static List<Wish> convertToWishList(WishLikeRequestDto requestDto, Long memberId, List<Long> existingArticleIds) {
        return requestDto.articleIds().stream()
                .filter(articleId -> !existingArticleIds.contains(articleId))
                .map(articleId -> Wish.from(memberId, articleId))
                .toList();
    }
}