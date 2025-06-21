package com.idol.board.service.myPage.command;

import com.idol.board.domain.entity.Participant;
import com.idol.board.repository.fanFal.ParticipantRepository;
import com.idol.board.usecase.mypage.command.DeleteMyPageUseCase;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMyPageService implements DeleteMyPageUseCase {

    private final MemberJpaRepository memberJpaRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public Long deleteMyFanFal(Long articleId, Long userId) {
        Participant participant = participantRepository.findParticipant(articleId,userId)
                .orElseThrow(() -> new NotFoundException("Participant", ""));


        participant.markAsDeleted();

        return 0L;
    }
}
