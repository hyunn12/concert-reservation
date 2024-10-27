package io.hhplus.reserve.reservation.interfaces.dto;

import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveRequest {

    @Getter
    @Builder
    @Schema(name = "ReserveRequest.Reserve", description = "예약 요청 객체")
    public static class Reserve {

        @NotNull
        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @NotNull
        @Schema(description = "좌석 ID 목록", example = "[1, 2, 3]")
        private List<Long> seatIdList;

        public ReserveCommand.Reserve toCommand() {
            return ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();
        }
    }

}
