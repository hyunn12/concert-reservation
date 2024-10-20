package io.hhplus.reserve.waiting.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingSchedulerService {

    private final WaitingRepository waitingRepository;

    public WaitingSchedulerService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    // 만료된 대기열 목록 조회
    public List<Waiting> getExpiredWaitingList() {
        return waitingRepository.getExpiredWaitingList();
    }

    // 저장
    public void saveAll(List<Waiting> waitingList) {
        waitingList.forEach(waitingRepository::saveWaiting);
    }

}
