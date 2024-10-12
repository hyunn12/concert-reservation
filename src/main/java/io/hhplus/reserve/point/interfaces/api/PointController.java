package io.hhplus.reserve.point.interfaces.api;

import io.hhplus.reserve.point.interfaces.dto.PointRequest;
import io.hhplus.reserve.point.interfaces.dto.PointResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
public class PointController {

    @GetMapping("/get/{id}")
    public PointResponse.Point getPoint(@PathVariable Long id) {
        // TODO 포인트 조회 API 작성

        return PointResponse.Point.builder()
                .userId(id)
                .point(10000)
                .build();
    }

    @PostMapping("/charge")
    public PointResponse.Charge charge(@Valid @RequestBody PointRequest.Charge request) {
        // TODO 포인트 충전 API 작성

        return PointResponse.Charge.builder()
                .point(request.getPoint())
                .build();
    }

}
