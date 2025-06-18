package com.idol.board.dto.response.participant;

import com.idol.board.domain.entity.Participant;
import jakarta.persistence.Column;

public record ParticipantResponseDto (
        String profile_image_url,
        String nickname
){

    public static ParticipantResponseDto from(Participant participant){
        return new ParticipantResponseDto(participant.getImageKey(), participant.getNickname());
    }
}
