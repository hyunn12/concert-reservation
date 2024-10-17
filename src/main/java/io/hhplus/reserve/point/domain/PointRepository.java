package io.hhplus.reserve.point.domain;

public interface PointRepository {

    Point getPointWithLock(Long userId);

    Point savePoint(Point point);
}
