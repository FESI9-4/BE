package com.idol.board.dto.response.participant;

import jakarta.persistence.Column;

public record ParticipantResponseDto (
        String profile_image_url,
        String nickname
){
}
