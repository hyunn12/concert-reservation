package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'ACTIVE' and w.updatedAt > CURRENT_TIMESTAMP - 5/1440")
    int countActiveByConcertId(long concertId);

    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'WAIT' and w.createdAt < :createdAt")
    int countWaitByConcertId(long concertId, LocalDateTime createdAt);

    Optional<Waiting> findByToken(String token);

}
