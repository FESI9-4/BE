package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.dto.request.article.ArticleCreateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.command.CreateArticleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateArticleService implements CreateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;

    @Override
    public Long createArticle(ArticleCreateRequestDto requestDto, Long writerId) {
        Location location = locationRepository.save(
                    Location.builder()
                            .latitude(requestDto.latitude())
                            .longitude(requestDto.longitude())
                            .roadNameAddress(requestDto.roadNameAddress())
                            .build());


        Article article = requestDto.toEntity(writerId,location.getLocationId());
        articleRepository.save(article);

        return article.getArticleId();
    }
}
