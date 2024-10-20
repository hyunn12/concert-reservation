package io.hhplus.reserve.waiting.domain;

import java.util.List;

public interface WaitingRepository {

    int getActiveCount(long concertId);

    int getWaitingCount(Waiting waiting);

    boolean isWaitingEmpty(Waiting waiting);

    Waiting getWaiting(String token);

    Waiting saveWaiting(Waiting waiting);

    List<Waiting> getExpiredWaitingList();

}
