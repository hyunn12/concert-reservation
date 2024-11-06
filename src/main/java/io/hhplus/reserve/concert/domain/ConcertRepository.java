package io.hhplus.reserve.concert.domain;

import java.util.List;

public interface ConcertRepository {

    List<Concert> getConcertList(String date);

    List<ConcertSeat> getConcertSeatListByConcertId(Long concertId);

    List<ConcertSeat> getConcertSeatListWithLock(List<Long> seatIdList);

    List<ConcertSeat> getConcertSeatList(List<Long> seatIdList);

    ConcertSeat getConcertSeat(Long seatId);

    List<ConcertSeat> saveConcertSeatList(List<ConcertSeat> seatList);

    Concert getConcert(Long concertId);

    Concert saveConcert(Concert concert);

}
