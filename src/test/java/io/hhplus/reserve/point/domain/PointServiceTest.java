package io.hhplus.reserve.point.domain;

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
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

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

            given(pointRepository.getPointWithLock(userId)).willReturn(mockPoint);

            // when
            PointInfo.Main result = pointService.getPointByUserId(userId);

            //then
            assertEquals(result.getPoint(), point);
            then(pointRepository).should(times(1)).getPointWithLock(userId);
        }

        @Test
        @DisplayName("존재하지않는 회원 포인트 조회 시 예외 발생")
        void testUserNotExist() {
            // given
            Long userId = 999L;
            given(pointRepository.getPointWithLock(userId)).willThrow(new EntityNotFoundException());

            // when / then
            assertThrows(EntityNotFoundException.class, () -> pointService.getPointByUserId(userId));

            then(pointRepository).should(times(1)).getPointWithLock(userId);
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
            PointCommand.Action command = PointCommand.Action.builder().userId(userId).point(chargePoint).build();

            given(pointRepository.getPointWithLock(userId)).willReturn(mockPoint);
            given(pointRepository.savePoint(mockPoint)).willReturn(mockPoint);

            // when
            PointInfo.Main result = pointService.chargePoint(command);

            // then
            assertEquals(result.getPoint(), orgPoint + chargePoint);
            then(pointRepository).should(times(1)).getPointWithLock(userId);
            then(pointRepository).should(times(1)).savePoint(mockPoint);
        }

        @Test
        @DisplayName("존재하지 않는 회원 포인트 충전 시 예외 발생")
        void shouldThrowExceptionWhenUserNotFoundForCharge() {
            // given
            Long userId = 999L;
            int chargePoint = 30000;

            PointCommand.Action command = PointCommand.Action.builder().userId(userId).point(chargePoint).build();

            given(pointRepository.getPointWithLock(userId)).willThrow(new EntityNotFoundException());

            // when / then
            assertThrows(EntityNotFoundException.class, () -> pointService.chargePoint(command));

            then(pointRepository).should(times(1)).getPointWithLock(userId);
            then(pointRepository).should(never()).savePoint(any(Point.class));
        }
    }

}