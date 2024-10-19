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

    public PointInfo.Main getPointByUserId(Long userId) {
        return PointInfo.Main.of(pointRepository.getPointByUserId(userId));
    }

    public PointInfo.Main chargePoint(PointCommand.Charge command) {
        Point point = pointRepository.getPointByUserId(command.getUserId());

        point.chargePoint(command.getPoint());
        pointRepository.savePoint(point);

        return PointInfo.Main.of(point);
    }

}
