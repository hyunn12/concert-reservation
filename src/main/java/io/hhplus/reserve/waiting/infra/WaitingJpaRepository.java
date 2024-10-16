package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    // todo 시간조건 추가

    // status가 active이면서 updatedAt 시간이 5분이 안지난 상태
    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'ACTIVE'")
    int countActiveByConcertId(long concertId);

    // status가 WAIT이면서 createdAt이 나보다 앞인거
    @Query("select count(w) from Waiting w where w.concertId = :concertId and w.status = 'WAIT'")
    int countWaitByConcertId(long concertId);

    Optional<Waiting> findByToken(String token);

}
