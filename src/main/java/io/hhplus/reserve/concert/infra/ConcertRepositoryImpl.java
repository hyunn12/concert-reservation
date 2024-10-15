package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertRepository;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository, ConcertSeatJpaRepository concertSeatJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
        this.concertSeatJpaRepository = concertSeatJpaRepository;
    }

    @Override
    public List<Concert> getConcertList(String date) {
        LocalDateTime parsedDate = LocalDateTime.parse(date);
        return concertJpaRepository.findAllByDate(parsedDate);
    }

    @Override
    public List<ConcertSeat> getConcertSeatListByConcertId(Long concertId) {
        return concertSeatJpaRepository.findAllByConcertId(concertId);
    }

}
