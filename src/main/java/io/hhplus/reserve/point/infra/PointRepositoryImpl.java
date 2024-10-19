package io.hhplus.reserve.point.infra;

import io.hhplus.reserve.point.domain.Point;
import io.hhplus.reserve.point.domain.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl(PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public Point getPointByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Point savePoint(Point point) {
        return pointJpaRepository.save(point);
    }

}
