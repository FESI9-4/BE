package com.idol.board.repository.fanFal;

import com.idol.board.domain.entity.Comment;
import com.idol.board.domain.entity.Participant;
import com.idol.board.domain.entity.QComment;
import com.idol.board.domain.entity.QParticipant;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ParticipantRepositoryImpl implements ParticipantRepositoryCustom{
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private static final QParticipant participant = QParticipant.participant;


    @Override
    public Optional<Participant> findParticipant(Long articleId, Long writerId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(participant)
                        .where(
                                participant.isDeleted.eq(false),
                                participant.articleId.eq(articleId),
                                participant.writerId.eq(writerId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<Participant> findParticipantFromArticle(Long articleId) {
        return queryFactory
                .selectFrom(participant)
                .where(
                        participant.isDeleted.eq(false),
                        participant.articleId.eq(articleId)
                ).fetch();
    }

    @Override
    public List<Participant> findParticipantFromWriterId(Long writerId) {
        return queryFactory
                .selectFrom(participant)
                .where(
                        participant.isDeleted.eq(false),
                        participant.writerId.eq(writerId)
                ).fetch();
    }

}
