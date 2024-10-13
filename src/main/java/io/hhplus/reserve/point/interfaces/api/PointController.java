package io.hhplus.reserve.point.interfaces.api;

import io.hhplus.reserve.point.interfaces.dto.PointRequest;
import io.hhplus.reserve.point.interfaces.dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@Tag(name = "Point", description = "포인트 관련 API")
public class PointController {

    @GetMapping("/get/{id}")
    @Operation(summary = "포인트 조회", description = "특정 회원이 가진 포인트 조회")
    public ResponseEntity<PointResponse.Point> getPoint(
            @Parameter(description = "회원 ID", example = "1", required = true)
            @PathVariable("id") Long userId
    ) {
        // TODO 포인트 조회 API 작성

        return ResponseEntity.ok(PointResponse.Point.builder()
                .point(10000)
                .build());
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "특정 회원의 포인트 충전")
    public ResponseEntity<PointResponse.Point> charge(
            @Valid @RequestBody PointRequest.Charge request
    ) {
        // TODO 포인트 충전 API 작성

        return ResponseEntity.ok(PointResponse.Point.builder()
                .point(request.getPoint())
                .build());
    }

}
