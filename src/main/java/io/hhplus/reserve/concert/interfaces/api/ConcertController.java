package io.hhplus.reserve.concert.interfaces.api;

import io.hhplus.reserve.concert.domain.ConcertInfo;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.concert.interfaces.dto.ConcertRequest;
import io.hhplus.reserve.concert.interfaces.dto.ConcertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concert")
@Tag(name = "Concert", description = "콘서트 관련 API")
public class ConcertController {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("/list/{date}")
    @Operation(summary = "예약 가능한 콘서트 목록 조회", description = "특정 날짜의 예약 가능한 콘서트 목록 조회")
    public ResponseEntity<List<ConcertResponse.Concert>> getList(
            @Parameter(description = "날짜 (형식: yyyy-MM-dd)", example = "2024-12-25", required = true)
            @PathVariable String date
    ) {

        List<ConcertInfo.ConcertDetail> resultList = concertService.getAvailableConcertList(date);

        return ResponseEntity.ok(resultList.stream().map(ConcertResponse.Concert::of).toList());
    }

    @GetMapping("/seat/list/{id}")
    @Operation(summary = "예약 가능한 좌석 목록 조회", description = "특정 콘서트의 예약 가능한 좌석 목록 조회")
    public ResponseEntity<List<ConcertResponse.Seat>> getSeatList(
            @Parameter(description = "콘서트 ID", example = "1", required = true)
            @PathVariable("id") Long concertId
    ) {

        List<ConcertInfo.SeatDetail> resultList = concertService.getSeatListByConcertId(concertId);

        return ResponseEntity.ok(resultList.stream().map(ConcertResponse.Seat::of).toList());
    }

    @PostMapping("/create")
    @Operation(summary = "콘서트 생성", description = "콘서트 새로 생성")
    public ResponseEntity<ConcertResponse.Concert> create(
            @Valid @RequestBody ConcertRequest.Create request
    ) {

        ConcertInfo.ConcertDetail result = concertService.createConcert(request.toCommand());

        return ResponseEntity.ok(ConcertResponse.Concert.of(result));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "콘서트 상세 조회", description = "콘서트 상세 조회")
    public ResponseEntity<ConcertResponse.Concert> getDetail(
            @Parameter(description = "콘서트 ID", example = "1", required = true)
            @PathVariable("id") Long concertId
    ) {
        ConcertInfo.ConcertDetail result = concertService.getConcert(concertId);

        return ResponseEntity.ok(ConcertResponse.Concert.of(result));
    }

    @GetMapping("/detail/temp/{id}")
    @Operation(summary = "콘서트 상세 조회", description = "콘서트 상세 조회")
    public ResponseEntity<ConcertResponse.Concert> getConcertById(
            @Parameter(description = "콘서트 ID", example = "1", required = true)
            @PathVariable("id") Long concertId
    ) {
        ConcertInfo.ConcertDetail result = concertService.getConcertById(concertId);

        return ResponseEntity.ok(ConcertResponse.Concert.of(result));
    }

}
