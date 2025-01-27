package io.hhplus.reserve.concert.infra;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertRepository;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import jakarta.persistence.EntityNotFoundException;
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
    public List<Concert> getConcertList(LocalDateTime startDate, LocalDateTime endDate) {
        return concertJpaRepository.findAllByDate(startDate, endDate);
    }

    @Override
    public List<ConcertSeat> getConcertSeatListByConcertId(Long concertId) {
        return concertSeatJpaRepository.findAllByConcertId(concertId);
    }

    @Override
    public List<ConcertSeat> getConcertSeatListWithLock(List<Long> seatIdList) {
        return concertSeatJpaRepository.findConcertSeatListWithLock(seatIdList);
    }

    @Override
    public List<ConcertSeat> getConcertSeatList(List<Long> seatIdList) {
        return concertSeatJpaRepository.findConcertSeatList(seatIdList);
    }

    @Override
    public ConcertSeat getConcertSeat(Long seatId) {
        return concertSeatJpaRepository.findById(seatId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<ConcertSeat> saveConcertSeatList(List<ConcertSeat> seatList) {
        return concertSeatJpaRepository.saveAll(seatList);
    }

    @Override
    public Concert getConcert(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Concert saveConcert(Concert concert) {
        return concertJpaRepository.save(concert);
    }

}
