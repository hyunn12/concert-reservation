package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.config.annotation.ApplicationService;

@ApplicationService
public class ReserveFacade {

    public void reserve(ReserveCommand.Reserve command) {

        // 토큰 유효성 조회

        // 좌석 예약 상태 확인

        // 기예약된 좌석이 하나라도 있는 경우 return

        // 모두 예약 가능한 경우 좌석 선점 및 선점 시각 기록

        // 토큰 updatedAt 변경

        // 좌석정보 가지고 예약 진행

        // 예약 넘길 때 seat status 한번 더 확인해서 AVAILABLE 인지

        // 결제

        // 예약 정보 반환

    }

}
