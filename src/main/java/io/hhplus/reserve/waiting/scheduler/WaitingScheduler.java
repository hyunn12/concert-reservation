package io.hhplus.reserve.waiting.scheduler;

import io.hhplus.reserve.waiting.domain.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingScheduler {

    private final WaitingService waitingService;

    // 대기열 활성화
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredWaiting() {
        log.info("# [WaitingScheduler] ::: Scheduler Start");
        waitingService.activeToken();
        log.info("# [WaitingScheduler] ::: Scheduler End");
    }

}
