package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query(value = "select * from concert c where c.reservation_start_at <= :endDate and c.reservation_end_at >= :startDate", nativeQuery = true)
    List<Concert> findAllByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
