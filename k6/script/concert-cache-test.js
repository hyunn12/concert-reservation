import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

const concertId = 24;

const cacheTrend = new Trend('cached_endpoint_time', true);
const nonCacheTrend = new Trend('non_cached_endpoint_time', true);

export const options = {
    vus: 100, // 가상 사용자 수
    duration: '1m', // 테스트 실행 시간
};

export default function () {
    const cachedRes = http.get(`http://localhost:8081/api/concert/detail/${concertId}`);
    check(cachedRes, {
        'cached response status is 200': (r) => r.status === 200,
    });
    cacheTrend.add(cachedRes.timings.duration);

    const nonCachedRes = http.get(`http://localhost:8081/api/concert/detail/temp/${concertId}`);
    check(nonCachedRes, {
        'non-cached response status is 200': (r) => r.status === 200,
    });
    nonCacheTrend.add(nonCachedRes.timings.duration);

    sleep(1);
}
