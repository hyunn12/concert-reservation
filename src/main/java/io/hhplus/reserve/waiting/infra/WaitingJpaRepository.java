package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'ACTIVE'")
    int countActiveByConcertId(@Param("concertId") long concertId);

    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'WAIT' and w.createdAt < :createdAt")
    int countWaitByConcertId(@Param("concertId") long concertId, @Param("createdAt") LocalDateTime createdAt);

    Optional<Waiting> findByToken(String token);

    @Query(value = "SELECT * FROM Waiting w WHERE w.status != 'DELETE' AND DATE_ADD(w.updated_at, INTERVAL 5 MINUTE) <= NOW()", nativeQuery = true)
    List<Waiting> findExpiredWaitingList();

}
