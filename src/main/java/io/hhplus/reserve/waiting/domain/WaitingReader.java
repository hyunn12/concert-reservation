package io.hhplus.reserve.waiting.domain;

public interface WaitingReader {

    int getActiveCount(long concertId);

    int getWaitingCount(long concertId);

    boolean isWaitingEmpty(long concertId);

    Waiting getWaiting(String token);

}
