package io.hhplus.reserve.reservation.infra;

import io.hhplus.reserve.reservation.domain.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationItemJpaRepository extends JpaRepository<ReservationItem, Long> {
}
