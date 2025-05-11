package com.jnulocker.announce.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateAnnounceRequest(
        @Schema(description = "공지사항 제목", example = "백도 사물함 신청 안내")
                @NotBlank(message = "공지사항 제목은 필수입니다.")
                @Size(max = 50, message = "공지사항 제목은 50자를 초과할 수 없습니다.")
                String title,
        @Schema(description = "공지사항 내용", example = "공지사항 내용입니다.")
                @NotBlank(message = "공지사항 내용은 필수입니다.")
                @Size(max = 1500, message = "공지사항 내용은 1500자를 초과할 수 없습니다.")
                String content,
        @Schema(description = "참여 학과/학부 ID 목록", example = "[1, 2, 3]")
                @NotEmpty(message = "참여 학과/학부는 최소 1개 이상이어야 합니다.")
                List<Long> participationDepartmentIds) {}
