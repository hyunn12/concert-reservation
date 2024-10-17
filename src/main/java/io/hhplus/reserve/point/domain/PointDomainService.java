package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.point.application.PointCommand;
import io.hhplus.reserve.point.application.PointInfo;
import org.springframework.stereotype.Service;

@Service
public class PointDomainService {

    private final PointRepository pointRepository;

    public PointDomainService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    // 사용자 포인트 조회
    public PointInfo.Main getPointByUserId(Long userId) {
        return PointInfo.Main.of(pointRepository.getPointByUserId(userId));
    }

    // 포인트 충전
    public PointInfo.Main chargePoint(PointCommand.Action command) {
        Point point = pointRepository.getPointByUserId(command.getUserId());

        point.chargePoint(command.getPoint());
        pointRepository.savePoint(point);

        return PointInfo.Main.of(point);
    }

    // 포인트 사용
    public void usePoint(PointCommand.Action command) {
        Point point = pointRepository.getPointByUserId(command.getUserId());
        point.usePoint(command.getPoint());
        pointRepository.savePoint(point);
    }

}
