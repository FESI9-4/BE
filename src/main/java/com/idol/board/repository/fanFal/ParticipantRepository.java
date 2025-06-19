package com.idol.board.repository.fanFal;

import com.idol.board.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>,ParticipantRepositoryCustom {

}
