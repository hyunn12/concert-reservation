package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertId(Long concertId);

}
