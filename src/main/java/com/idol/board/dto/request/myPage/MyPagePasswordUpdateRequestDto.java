package com.idol.board.dto.request.myPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MyPagePasswordUpdateRequestDto(
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
        String password,

        @NotBlank(message = "비밀번호는 필수 값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
        String newPassword
) {
}
