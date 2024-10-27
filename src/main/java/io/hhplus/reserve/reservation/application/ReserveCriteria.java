package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveCriteria {

    @Getter
    @Builder
    public static class Main {
        private Long userId;
        private List<Long> seatIdList;

        public static ReserveCriteria.Main create(ReserveCommand.Reserve command) {
            return ReserveCriteria.Main.builder()
                    .userId(command.getUserId())
                    .seatIdList(command.getSeatIdList())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Reserve {
        private Long userId;
        private List<ConcertSeat> seatList;
        private Concert concert;

        public static ReserveCriteria.Reserve create(Long userId, List<ConcertSeat> seatList, Concert concert) {
            return Reserve.builder()
                    .userId(userId)
                    .seatList(seatList)
                    .concert(concert)
                    .build();
        }
    }

}
