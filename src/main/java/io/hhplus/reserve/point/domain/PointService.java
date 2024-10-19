package io.hhplus.reserve.point.domain;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    // 사용자 포인트 조회
    public PointInfo.Main getPointByUserId(Long userId) {
        return PointInfo.Main.of(pointRepository.getPointWithLock(userId));
    }

    // 포인트 충전
    @Transactional
    public PointInfo.Main chargePoint(PointCommand.Action command) {
        Point point = pointRepository.getPointWithLock(command.getUserId());

        point.chargePoint(command.getPoint());
        pointRepository.savePoint(point);

        return PointInfo.Main.of(point);
    }

    // 포인트 사용
    @Transactional
    public void usePoint(PointCommand.Action command) {
        Point point = pointRepository.getPointWithLock(command.getUserId());
        point.usePoint(command.getPoint());
        pointRepository.savePoint(point);
    }

}
