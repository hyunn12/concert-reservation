package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE DATE(:date) BETWEEN DATE(c.reservationStartAt) AND DATE(c.reservationEndAt)")
    List<Concert> findAllByDate(LocalDate date);

}
