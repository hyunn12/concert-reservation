package io.hhplus.reserve.outbox.scheduler;

import io.hhplus.reserve.outbox.application.OutboxFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxFacade outboxFacade;

    // 재시도 (5 min)
    @Scheduled(fixedRate = 300000)
    public void retrySendOutboxMessage() {
        log.info("# [OutboxScheduler] ::: Scheduler Start");
        outboxFacade.retrySendOutboxMessage();
        log.info("# [OutboxScheduler] ::: Scheduler End");
    }
}
