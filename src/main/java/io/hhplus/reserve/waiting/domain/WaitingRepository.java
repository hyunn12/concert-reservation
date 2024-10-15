package io.hhplus.reserve.waiting.domain;

public interface WaitingRepository {

    int getActiveCount(long concertId);

    int getWaitingCount(long concertId);

    boolean isWaitingEmpty(long concertId);

    Waiting getWaiting(String token);

    Waiting createWaiting(Waiting waiting);

}
