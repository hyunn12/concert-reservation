package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public static final String CONCERT_CACHE_NAME = "concertDetailCache";
    public static final String CONCERT_CACHE_PREFIX = "concert:";
    public static final Long CONCERT_CACHE_TTL = 30L;

    // 콘서트 목록 조회
    public List<ConcertInfo.ConcertDetail> getAvailableConcertList(String date) {
        List<Concert> concertList = concertRepository.getConcertList(date);
        return concertList.stream().map(ConcertInfo.ConcertDetail::of).toList();
    }

    // 콘서트 좌석 목록 조회
    public List<ConcertInfo.SeatDetail> getSeatListByConcertId(Long concertId) {
        List<ConcertSeat> seatList = concertRepository.getConcertSeatListByConcertId(concertId);
        return seatList.stream().map(ConcertInfo.SeatDetail::of).toList();
    }

    // 콘서트 상세 조회 (redis 캐시)
    @Transactional
    public ConcertInfo.ConcertDetail getConcert(Long concertId) {
        String cacheKey = CONCERT_CACHE_NAME + "::" + CONCERT_CACHE_PREFIX + concertId;

        ConcertInfo.ConcertDetail result = (ConcertInfo.ConcertDetail) redisTemplate.opsForValue().get(cacheKey);
        if (result != null) {
            return result;
        }

        Concert concert = getConcertDetail(concertId);
        ConcertInfo.ConcertDetail concertDetail = ConcertInfo.ConcertDetail.of(concert);
        redisTemplate.opsForValue().set(cacheKey, concertDetail, CONCERT_CACHE_TTL, TimeUnit.MINUTES);

        return concertDetail;
    }

    // 콘서트 상세 조회 (DB)
    @Transactional
    public ConcertInfo.ConcertDetail getConcertById(Long concertId) {
        Concert concert = getConcertDetail(concertId);
        return ConcertInfo.ConcertDetail.of(concert);
    }

    // 콘서트 상세조회
    public Concert getConcertDetail(Long concertId) {
        return concertRepository.getConcert(concertId);
    }

    // 콘서트 좌석 목록 조회 (Optimistic Lock)
    @Transactional
    public List<ConcertSeat> getSeatListWithLock(List<Long> seatIdList) {
        return concertRepository.getConcertSeatListWithLock(seatIdList);
    }

    @DistributedLock(key = "'seatLock:' + #concertId + #seatIdList.sort()")
    public List<ConcertSeat> getSeatListWithRedis(List<Long> seatIdList) {
        return concertRepository.getConcertSeatList(seatIdList);
    }

    @DistributedLock(key = "'seatLock:' + #concertId + ':' + #seatId")
    public ConcertSeat getConcertSeatWithRedis(Long concertId, Long seatId) {
        return concertRepository.getConcertSeat(seatId);
    }

    // 콘서트 좌석 선점
    @Transactional
    public void reserveSeat(List<ConcertSeat> seatList) {
        seatList.forEach(ConcertSeat::reserveSeat);
        concertRepository.saveConcertSeatList(seatList);
    }

    // 콘서트 좌석 선점상태 확인
    public void checkSeatExpired(List<ConcertSeat> seatList) {
        seatList.forEach(ConcertSeat::checkSeatExpired);
    }

    // 콘서트 좌석 확정
    @Transactional
    public void confirmSeat(List<ConcertSeat> seatList) {
        seatList.forEach(ConcertSeat::confirm);
    }

    // 콘서트 생성
    @Transactional
    public ConcertInfo.ConcertDetail createConcert(ConcertCommand.Create command) {

        Concert concert = Concert.createBuilder().command(command).build();
        concertRepository.saveConcert(concert);

        ConcertInfo.ConcertDetail concertDetail = ConcertInfo.ConcertDetail.of(concert);

        String cacheKey = CONCERT_CACHE_NAME + "::" + CONCERT_CACHE_PREFIX + concert.getConcertId();
        redisTemplate.opsForValue().set(cacheKey, concertDetail, CONCERT_CACHE_TTL, TimeUnit.MINUTES);

        return concertDetail;
    }

}
