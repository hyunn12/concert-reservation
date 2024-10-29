package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.point.infra.PointJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class PointServiceIntegrationTest extends TestContainerSupport {

    // orm --
    @Autowired
    private PointJpaRepository pointJpaRepository;

    // sut --
    @Autowired
    private PointService pointService;

    private final Long pointId = 1L;
    private final Long userId = 1L;
    private final int initPoint = 10000;

    private Point point;

    @BeforeEach
    void setUp() {
        point = new Point(pointId, userId, initPoint);
        pointJpaRepository.save(point);
    }

    @Test
    @DisplayName("유저 포인트 조회")
    void testGetUserPoint() {
        PointInfo.Main result = pointService.getPointByUserId(userId);
        assertNotNull(result);
    }

    @Test
    @DisplayName("포인트 충전")
    void testChargePoint() {
        int chargePoint = 10000;

        PointCommand.Action command = PointCommand.Action.builder()
                .userId(userId)
                .point(chargePoint)
                .build();
        PointInfo.Main result = pointService.chargePoint(command);

        assertNotNull(result);
        assertEquals(result.getPoint(), initPoint + chargePoint);
    }

}