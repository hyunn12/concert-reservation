package io.hhplus.reserve.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Test
    @DisplayName("유저 포인트 조회")
    void testGetUserPoint() {
        Long userId = 1L;
        PointInfo.Main result = pointService.getPointByUserId(userId);
        assertNotNull(result);
    }

    @Test
    @DisplayName("포인트 충전")
    void testChargePoint() {
        Long userId = 1L;
        Point point = pointRepository.getPointWithLock(userId);
        int orgPoint = point.getPoint();

        PointCommand.Action command = PointCommand.Action.builder()
                .userId(1L)
                .point(10000)
                .build();
        PointInfo.Main result = pointService.chargePoint(command);

        assertNotNull(result);
        assertEquals(result.getPoint(), orgPoint + 10000);
    }

}