package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findAllByConcertId(Long concertId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT cs FROM ConcertSeat cs WHERE cs.seatId IN :seatIdList")
    List<ConcertSeat> findConcertSeatListWithLock(@Param("seatIdList") List<Long> seatIdList);

}
