package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.point.application.PointCommand;
import io.hhplus.reserve.point.application.PointInfo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PointDomainServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointDomainService pointDomainService;

    @Nested
    @DisplayName("포인트 조회")
    class GetPoint {

        @Test
        @DisplayName("존재하는 회원id로 포인트 조회")
        void testUserExistGetPoint() {
            // given
            Long userId = 1L;
            int point = 1000;

            Point mockPoint = new Point(1L, userId, point);

            given(pointRepository.getPointByUserId(userId)).willReturn(mockPoint);

            // when
            PointInfo.Main result = pointDomainService.getPointByUserId(userId);

            //then
            assertEquals(result.getPoint(), point);
            then(pointRepository).should(times(1)).getPointByUserId(userId);
        }

        @Test
        @DisplayName("존재하지않는 회원 포인트 조회 시 예외 발생")
        void testUserNotExist() {
            // given
            Long userId = 999L;
            given(pointRepository.getPointByUserId(userId)).willThrow(new EntityNotFoundException());

            // when / then
            assertThrows(EntityNotFoundException.class, () -> {
                pointDomainService.getPointByUserId(userId);
            });

            then(pointRepository).should(times(1)).getPointByUserId(userId);
        }

    }

    @Nested
    @DisplayName("포인트 충전")
    class ChargePoint {

        @Test
        @DisplayName("포인트 충전 성공")
        void chargePoint_ValidCharge_Success() {
            // given
            Long userId = 1L;
            int orgPoint = 10000;
            int chargePoint = 30000;

            Point mockPoint = new Point(1L, userId, orgPoint);
            PointCommand.Charge command = PointCommand.Charge.builder().userId(userId).point(chargePoint).build();

            given(pointRepository.getPointByUserId(userId)).willReturn(mockPoint);
            given(pointRepository.savePoint(mockPoint)).willReturn(mockPoint);

            // when
            PointInfo.Main result = pointDomainService.chargePoint(command);

            // then
            assertEquals(result.getPoint(), orgPoint + chargePoint);
            then(pointRepository).should(times(1)).getPointByUserId(userId);
            then(pointRepository).should(times(1)).savePoint(mockPoint);
        }

        @Test
        @DisplayName("존재하지 않는 회원 포인트 충전 시 예외 발생")
        void shouldThrowExceptionWhenUserNotFoundForCharge() {
            // given
            Long userId = 999L;
            int chargePoint = 30000;

            PointCommand.Charge command = PointCommand.Charge.builder().userId(userId).point(chargePoint).build();

            given(pointRepository.getPointByUserId(userId)).willThrow(new EntityNotFoundException());

            // when / then
            assertThrows(EntityNotFoundException.class, () -> {
                pointDomainService.chargePoint(command);
            });

            then(pointRepository).should(times(1)).getPointByUserId(userId);
            then(pointRepository).should(never()).savePoint(any(Point.class));
        }
    }

}