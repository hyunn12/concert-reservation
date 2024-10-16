package io.hhplus.reserve.point.domain;

public interface PointRepository {

    Point getPointByUserId(Long userId);

    Point savePoint(Point point);
}
