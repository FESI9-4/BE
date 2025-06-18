package com.idol.board.service.article.command;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.UseStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Participant;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.fanFal.ParticipantRepository;
import com.idol.board.usecase.article.command.ParticipantUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.global.exception.ForbiddenException;
import com.idol.global.exception.NotFoundException;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService implements ParticipantUseCase {
    private final ParticipantRepository participantRepository;
    private final ArticleRepository articleRepository;
    private final MemberJpaRepository memberJpaRepository;


    @Override
    public Long joinFanFal(Long articleId, Long writerId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        String memberImageKey = null;

            if (article.getUseStatus() != UseStatus.COMPLETED_STATUS) {
                article.upCurrentPerson();
                if (article.getCurrentPerson() == article.getMinPerson()) {
                    article.updateOpenStatus(OpenStatus.CONFIRMED_STATUS);
                }

                Member member = memberJpaRepository.findById(writerId).orElseThrow(() -> new NotFoundException("Writer", writerId));
                if (member.getProfileImgUrl() != null) {
                    memberImageKey = member.getProfileImgUrl();
                }

                if (article.getCurrentPerson() == article.getMaxPerson()) {
                    article.updateUseStatus(UseStatus.COMPLETED_STATUS);
                }

                return participantRepository.save(new Participant(article.getArticleId(), memberImageKey, member.getNickname(), writerId)).getParticipantId();
            } else {
                throw new ForbiddenException("Participant", articleId);
            }
    }

    @Override
    public Long cancelFanFal(Long articleId, Long writerId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        Participant participant = participantRepository.findParticipant(articleId,writerId)
                .orElseThrow(() -> new NotFoundException("Participant", writerId));

        if(participant != null){
            participant.markAsDeleted();
            article.downCurrentPerson();

            if(article.getCurrentPerson() < article.getMinPerson()){
                article.updateOpenStatus(OpenStatus.PENDING_STATUS);
            }
        }


        return participant.getParticipantId();
    }
}
