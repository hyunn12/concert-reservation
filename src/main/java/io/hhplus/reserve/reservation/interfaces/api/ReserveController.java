package io.hhplus.reserve.reservation.interfaces.api;

import io.hhplus.reserve.reservation.application.ReserveFacade;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.hhplus.reserve.reservation.interfaces.dto.ReserveRequest;
import io.hhplus.reserve.reservation.interfaces.dto.ReserveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reserve")
@Tag(name = "Reserve", description = "예약 관련 API")
public class ReserveController {

    private final ReserveFacade reserveFacade;

    public ReserveController(ReserveFacade reserveFacade) {
        this.reserveFacade = reserveFacade;
    }

    @PostMapping("/reserve")
    @Operation(summary = "좌석 선점 요청", description = "특정 좌석에 대한 선점 요청")
    public ResponseEntity<ReserveResponse.Reserve> reserve(
            @RequestBody ReserveRequest.Reserve request
    ) {

        ReserveInfo.Reserve result = reserveFacade.reserve(request.toCommand());

        return ResponseEntity.ok(ReserveResponse.Reserve.of(result));
    }

}
