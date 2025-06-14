package com.idol.board.service.article.query;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.domain.entity.Participant;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.participant.ParticipantResponseDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.global.exception.NotFoundException;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadArticleService implements ReadArticleUseCase {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepsitory;
    private final ImageGetUserCase imageGetUserCase;


    @Override
    public ArticleReadResponseDto readArticle(Long articleId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));


        /* TODO::
           1. Participant 리스트 호출
           2. 현재 게시물 찜되어 있는지 확인
         */

        GetS3UrlDto getS3UrlDto = imageGetUserCase.getGetS3Url(article.getArticleImageKey());
        Location location = locationRepsitory.findByLocationId(article.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location", article.getLocationId()));

        List<ParticipantResponseDto> participants = null;

        validateCheckOpenStatus(article);

        ArticleReadResponseDto dto = ArticleReadResponseDto.from(article,location,participants,true,getS3UrlDto.preSignedUrl());

        return dto;
    }

    private void validateCheckOpenStatus(Article article) {
        if(article.getCurrentPerson() >= article.getMinPerson()){
            article.updateOpenStatus(OpenStatus.CONFIRMED_STATUS);
        }
    }
}
