package io.hhplus.reserve.waiting.domain;

public interface WaitingRepository {

    int getActiveCount(long concertId);

    int getWaitingCount(Waiting waiting);

    boolean isWaitingEmpty(Waiting waiting);

    Waiting getWaiting(String token);

    Waiting saveWaiting(Waiting waiting);

}
