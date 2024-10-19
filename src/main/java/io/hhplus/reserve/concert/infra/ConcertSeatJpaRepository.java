package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertId(Long concertId);

}
