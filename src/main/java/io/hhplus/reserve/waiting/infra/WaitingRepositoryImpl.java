package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.WaitingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.hhplus.reserve.waiting.domain.WaitingConstant.*;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final StringRedisTemplate redisTemplate;
    private ZSetOperations<String, String> zSetOperations;
    private SetOperations<String, String> setOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public long getActiveCount() {
        return Objects.requireNonNull(redisTemplate.keys(ACTIVE_KEY_PREFIX + "*")).size();
    }

    @Override
    public void addActiveQueue(String token) {
        String key = ACTIVE_KEY_PREFIX + token;
        setOperations.add(key, token);
        setOperations.getOperations().expire(key, WAITING_TTL, TimeUnit.MINUTES);
    }

    @Override
    public void addWaitingQueue(String token) {
        long score = System.currentTimeMillis();
        zSetOperations.add(WAITING_KEY, token, score);
    }

    @Override
    public long getWaitingRank(String token) {
        Long rank = zSetOperations.rank(WAITING_KEY, token);
        return rank == null ? 0 : rank;
    }

    @Override
    public void removeActiveQueue(String token) {
        String key = ACTIVE_KEY_PREFIX + token;
        setOperations.remove(key, token);
    }

    @Override
    public void removeWaitingQueue(String token) {
        zSetOperations.remove(WAITING_KEY, token);
    }

    @Override
    public List<String> popWaitingTokens() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.popMin(WAITING_KEY, ACTIVE_SIZE);
        return Optional.ofNullable(typedTuples)
                .map(set -> set.stream()
                        .map(ZSetOperations.TypedTuple::getValue)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean isActiveToken(String token) {
        String key = ACTIVE_KEY_PREFIX + token;
        return !Objects.requireNonNull(setOperations.members(key)).isEmpty();
    }

}
