package io.hhplus.reserve.waiting.domain;

import java.util.List;

public interface WaitingRepository {

    long getActiveCount();

    void addActiveQueue(String token);

    void addWaitingQueue(String token);

    long getWaitingRank(String token);

    void removeActiveQueue(String token);

    void removeWaitingQueue(String token);

    List<String> popWaitingTokens();

    boolean isActiveToken(String token);

}
