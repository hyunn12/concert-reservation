package io.hhplus.reserve.waiting.domain;

public interface WaitingRepository {

    long getActiveCount(String key);

    void addActiveQueue(String token);

    void addWaitingQueue(String token, Long concertId);

    long getWaitingCount(String token);

}
