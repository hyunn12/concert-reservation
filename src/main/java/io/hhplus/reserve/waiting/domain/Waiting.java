package io.hhplus.reserve.waiting.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting {

    private Long waitingId;
    private String token;
    private Long concertId;
    private WaitingStatus status;
    private long waitingCount;
    private long waitingTime; // 대기시간 (s)

}
