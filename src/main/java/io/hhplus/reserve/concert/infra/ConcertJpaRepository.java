package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query(value = "SELECT * FROM Concert c WHERE DATE(:date) BETWEEN DATE(c.reservation_start_at) AND DATE(c.reservation_end_at)", nativeQuery = true)
    List<Concert> findAllByDate(@Param("date") LocalDate date);

}
