package com.idol.board.repository.fanFal;

import com.idol.board.domain.entity.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepositoryCustom {
    Optional<Participant> findParticipant(Long articleId, Long writerId);

    List<Participant> findParticipantFromArticle(Long articleId);
}
