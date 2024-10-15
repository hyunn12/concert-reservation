package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE DATE(:date) BETWEEN DATE(c.reservationStartAt) AND DATE(c.reservationEndAt)")
    List<Concert> findAllByDate(LocalDateTime date);

}
