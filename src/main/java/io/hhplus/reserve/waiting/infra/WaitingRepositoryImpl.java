package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.WaitingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.hhplus.reserve.waiting.domain.WaitingConstant.ACTIVE_KEY;
import static io.hhplus.reserve.waiting.domain.WaitingConstant.WAITING_KEY;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;

    private final StringRedisTemplate redisTemplate;
    private ZSetOperations<String, String> zSetOperations;
    private SetOperations<String, String> setOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public long getActiveCount(String key) {
        return Objects.requireNonNull(redisTemplate.keys(key + "*")).size();
    }

    @Override
    public void addActiveQueue(String token) {
        String key = ACTIVE_KEY + token;
        setOperations.add(key, token);
        setOperations.getOperations().expire(key, 5, TimeUnit.MINUTES);
    }

    @Override
    public void addWaitingQueue(String token, Long concertId) {
        long score = System.currentTimeMillis();
        zSetOperations.add(WAITING_KEY, token, score);
    }

    @Override
    public long getWaitingCount(String token) {
        return zSetOperations.rank(WAITING_KEY, token);
    }

}
