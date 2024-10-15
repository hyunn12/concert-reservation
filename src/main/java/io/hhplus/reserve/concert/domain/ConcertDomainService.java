package io.hhplus.reserve.concert.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertDomainService {

    private final ConcertReader concertReader;

    public ConcertDomainService(ConcertReader concertReader) {
        this.concertReader = concertReader;
    }

    public List<Concert> getAvailableConcertList(String date) {
        return concertReader.getConcertList(date);
    }

    public List<ConcertSeat> getSeatListByConcertId(Long concertId) {
        return concertReader.getConcertSeatListByConcertId(concertId);
    }

}
