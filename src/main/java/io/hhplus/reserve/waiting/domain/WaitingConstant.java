package io.hhplus.reserve.waiting.domain;

public class WaitingConstant {

    public static final String WAITING_KEY = "waiting";
    public static final String ACTIVE_KEY_PREFIX = "active:";

    // 대기열 최대 인원 수
    public static final long ACTIVE_SIZE = 1000L;

    // 최대 대기 시간
    public static final long WAITING_TTL = 5L;

}
