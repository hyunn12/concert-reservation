package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.concert.application.ConcertInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertDomainService {

    private final ConcertRepository concertRepository;

    public ConcertDomainService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<ConcertInfo.ConcertDetail> getAvailableConcertList(String date) {
        List<Concert> concertList = concertRepository.getConcertList(date);

        return concertList.stream()
                .map(ConcertInfo.ConcertDetail::of)
                .toList();
    }

    public List<ConcertInfo.SeatDetail> getSeatListByConcertId(Long concertId) {
        List<ConcertSeat> seatList = concertRepository.getConcertSeatListByConcertId(concertId);

        return seatList.stream()
                .map(ConcertInfo.SeatDetail::of)
                .toList();
    }

}
