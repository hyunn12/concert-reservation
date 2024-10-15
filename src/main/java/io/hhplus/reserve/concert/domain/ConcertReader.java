package io.hhplus.reserve.concert.domain;

import java.util.List;

public interface ConcertReader {

    List<Concert> getConcertList(String date);

    List<ConcertSeat> getConcertSeatListByConcertId(Long concertId);

}
