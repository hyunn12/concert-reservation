package io.hhplus.reserve.waiting.scheduler;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WaitingScheduler {

    private final WaitingSchedulerService schedulerService;

    public WaitingScheduler(WaitingSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    // 대기열 이탈 확인 (10 min)
    @Scheduled(fixedRate = 600000)
    public void deleteExpiredWaiting() {
        log.info("# [WaitingScheduler] ::: Scheduler Start");

        List<Waiting> waitingList = schedulerService.getExpiredWaitingList();

        waitingList.forEach(Waiting::deleteToken);

        schedulerService.saveAll(waitingList);

        log.info("# [WaitingScheduler] ::: Scheduler End");
    }

}
