## 부하 테스트 결과 및 개선 방안 정리

> 이 문서는 현재 애플리케이션의 주요 기능에 대해 부하테스트를 수행하고, 성능을 분석한 문서이다.


<details>
<summary>목차</summary>

- [부하테스트 목적](#부하테스트-목적)
- [테스트 케이스](#테스트-케이스)
    - [Load Test](#load-test--부하-테스트-)
    - [Endurance Test](#endurance-test--내구성-테스트-)
    - [Stress Test](#stress-test--스트레스-테스트-)
    - [Peak Test](#peak-test--최고-부하-테스트-)
    - [현재 애플리케이션에 적용할 테스트 케이스](#현재-애플리케이션에-적용할-테스트-케이스)
- [테스트 환경](#테스트-환경)
- [테스트 시나리오](#테스트-시나리오)
  - [토큰 발급](#토큰-발급)
  - [좌석 선점](#좌석-선점)
- [부하테스트 진행](#부하테스트-진행)
  - [1-1. 토큰 발급 load test](#1-1-토큰-발급-load-test)
  - [1-2. 토큰발급 peak test](#1-2-토큰발급-peak-test)
  - [2. 좌석 선점 load test](#2-좌석-선점-load-test)
- [테스트 결과](#테스트-결과)
    - [1-1. 토큰 발급 load test](#1-1-토큰-발급-load-test-1)
    - [1-2. 토큰발급 peak test](#1-2-토큰발급-peak-test-1)
    - [2. 좌석 선점 load test](#2-좌석-선점-load-test-1)
- [결론](#결론)
  - [테스트 결과 요약](#테스트-결과-요약-토큰발급)
  - [개선 방안](#개선-방안)
  - [향후 계획](#향후-계획)
</details>

<br>

---

<br>

### 부하테스트 목적
부하테스트란 시스템이나 애플리케이션이 예상되는 부하를 처리할 수 있는지 측정하기 위해 실시하는 테스트이다.

부하테스트를 통해 시스템의 성능을 측정하고 잠재적인 문제를 미리 해결할 수 있다.    

현재 작성된 애플리케이션에 대해 테스트 결과를 토대로 개선할 점을 정리하고, 성능 최적화 방법을 고안하고자 한다.


### 테스트 케이스

부하테스트는 진행 시간과 트래픽 양에 따라 아래 4가지 포맷으로 분류한다.

#### Load Test ( 부하 테스트 )
- 시스템이 예상되는 부하를 정상적으로 처리할 수 있는지 평가
- 특정한 부하를 제한된 시간 동안 제공해 이상이 없는지 파악
- 목표치를 설정해 적정한 Application 배포 Spec 또한 고려해 볼 수 있음

#### Endurance Test ( 내구성 테스트 )
- 시스템이 장기간 동안 안정적으로 운영될 수 있는지 평가
- 특정한 부하를 장기간 동안 제공했을 때, 발생하는 문제가 있는지 파악
- 장기적으로 Application 을 운영할 때 발생할 수 있는 숨겨진 문제를 파악해 볼 수 있음 ( feat. Memory Leak, Slow Query 등 )

#### Stress Test ( 스트레스 테스트 )
- 시스템이 지속적으로 증가하는 부하를 얼마나 잘 처리할 수 있는지 평가
- 점진적으로 부하를 증가시켰을 때, 발생하는 문제가 있는지 파악
- 장기적으로 Application 을 운영하기 위한 Spec 및 확장성과 장기적인 운영 계획을 파악해 볼 수 있음

#### Peak Test ( 최고 부하 테스트 )
- 시스템에 일시적으로 많은 부하가 가해졌을 때, 잘 처리하는지 평가
- 목표치로 설정한 임계 부하를 일순간에 제공했을 때, 정상적으로 처리해내는지 파악
- 선착순 이벤트 등을 준비하면서 정상적으로 서비스를 제공할 수 있을지 파악해 볼 수 있음

#### 현재 애플리케이션에 적용할 테스트 케이스

현재 애플리케이션에 작성된 로직 중 부하가 발생할 것으로 예상되는 케이스는 아래와 같다.

- **대기열 발급 및 갱신**
  - peak test: 이벤트 등의 이유로 특정 시간대에 트래픽이 몰리는 상황을 가정
  - load test: 평소 대기열 트래픽을 가정
- **좌석선점**
  - load test: 좌석 선점에 대한 트래픽은 대기열보다 분산될 가능성이 있다고 생각


<br>

### 테스트 환경
아래 테스트 결과는 로컬 환경에서 수행된 결과이며, 실제 배포 환경과 다를 수 있다.      
테스트 환경은 아래와 같다.

- **OS**: macOS
- **CPU**: Apple M1 Pro
- **Memory**: 32GB RAM
- **Storage**: 512GB SSD
- **Containerization Platform**: Docker
- **Load Testing Tool**: K6


<br>

### 테스트 시나리오

각 테스트의 시나리오는 다음과 같다.
실제 유저가 각 기능에 접근했을 때 사용될 API 목록을 기준으로 한다.

#### 토큰 발급
```text
토큰 발급 -> 토큰 조회
```

#### 좌석 선점
```text
콘서트 목록 조회 -> 좌석 목록 조회 -> 토큰 발급 -> 좌석 선점
```


<br>

### 부하테스트 진행

`k6` 사용하여 로컬 환경에서 부하테스트를 진행하도록 한다.

#### 1-1. 토큰 발급 load test
<details>
<summary>토큰 발급 load test k6 script</summary>

```js
import http from 'k6/http';
import { check } from 'k6';

export let options = {
    stages: [
        {duration: '1s', target: 5},
        { duration: '5s', target: 100 },
        { duration: '5s', target: 200 },
        { duration: '5s', target: 300 },
        { duration: '5s', target: 500 },
        { duration: '10s', target: 600 },
        { duration: '10s', target: 500 },
        { duration: '5s', target: 0 }
    ]
};

export default function () {
    let token = getToken();
    checkToken(token)
}

function getToken() {
    const res = http.get('http://localhost:8081/api/token/check');

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        if (Array.isArray(responseData) && responseData[1] && responseData[1].token) {
            return responseData[1].token;
        } else {
            console.error('Unexpected response structure:', res.body);
            return null;
        }
    } else {
        console.log(`Request failed with status: ${res.status}`);
        return null;
    }
}

function checkToken(token) {
    const headers = {
        'Content-Type': 'application/json',
        'token': `${token}`,
    };
    const res = http.get('http://localhost:8081/api/token/check', { headers });

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        console.log(`Response Body: ${res.body}`);
    } else {
        console.log(`Request failed with status: ${res.status}`);
        console.log(`Response Body: ${res.body}`);
    }
}
```

</details>

#### 1-2. 토큰발급 peak test
<details>
<summary>토큰 발급 peak test k6 script</summary>

```js
import http from 'k6/http'
import { check } from 'k6'

export let options = {
    stages: [
        { duration: '5s', target: 10 },
        { duration: '10s', target: 1000 },
        { duration: '30s', target: 1000 },
        { duration: '5s', target: 0 },
    ],
}

export default function () {
    let token = getToken()
    checkToken(token)
}

function getToken() {
    const res = http.get('http://localhost:8081/api/token/check')

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200})

    if (isStatus200) {
        let responseData = JSON.parse(res.body)
        if (Array.isArray(responseData) && responseData[1] && responseData[1].token) {
            return responseData[1].token
        } else {
            console.error('Unexpected response structure:', res.body)
            return null
        }
    } else {
        console.log(`Request failed with status: ${res.status}`)
        return null
    }
}

function checkToken(token) {
    const headers = {
        'Content-Type': 'application/json',
        'token': `${token}`,
    }
    const res = http.get('http://localhost:8081/api/token/check', { headers })

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200})

    if (isStatus200) {
        console.log(`Response Body: ${res.body}`)
    } else {
        console.log(`Request failed with status: ${res.status}`)
        console.log(`Response Body: ${res.body}`)
    }
}
```
</details>

#### 2. 좌석 선점 load test
<details>
<summary>좌석 선점 load test</summary>

```js
import http from 'k6/http';
import {check, sleep, group} from 'k6';
import {randomItem, randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export let options = {
    stages: [
        {duration: '1s', target: 5},
        { duration: '5s', target: 100 },
        { duration: '5s', target: 200 },
        { duration: '5s', target: 300 },
        { duration: '5s', target: 500 },
        { duration: '10s', target: 600 },
        { duration: '10s', target: 500 },
        { duration: '5s', target: 0 }
    ],
};

export default function () {
    group('Reservation Load Test', function () {

        // 콘서트 목록 조회
        let concertList = group('Get Concert List', function () {
            return getConcertList()
        })
        if (!concertList || concertList.length === 0) {
            console.log('No concerts available.')
            return
        }

        sleep(3);

        // 좌석 목록 조회
        const concertId = concertList.get(0)
        let seatList = group('Get Seat List', function () {
            return getSeatList(concertId)
        })
        if (!seatList || seatList.length === 0) {
            console.log('No concert seats available.')
            return
        }

        sleep(3);

        // 토큰 발급
        let token = group('Get Token', function () {
            return getToken();
        });
        if (!token) {
            return;
        }

        sleep(3);

        // 좌석 예약
        group('Make Reservation', function () {
            let userId = randomIntBetween(1, 100000);
            let selectedSeats = selectRandomSeats(seatList);
            reservationSeat(token, concertId, selectedSeats, userId);
        });
    });
}

function getConcertList() {
    const date = '2024-01-01'
    let res = http.get(`http://localhost:8081/api/concert/list/${date}`);

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        return responseData.content;
    } else {
        console.log(`Failed to fetch concert list. Status: ${res.status}`);
        return null;
    }
}

function getSeatList(concertId) {
    let res = http.get(`http://localhost:8081/api/concert/seat/list/${concertId}`);

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        return responseData.content;
    } else {
        console.log(`Failed to fetch concert list. Status: ${res.status}`);
        return null;
    }
}

function getToken() {
    const res = http.get('http://localhost:8081/api/token/check')

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200})


    if (isStatus200) {
        let responseData = JSON.parse(res.body)
        if (Array.isArray(responseData) && responseData[1] && responseData[1].token) {
            return responseData[1].token
        } else {
            console.error('Unexpected response structure:', res.body)
            return null
        }
    } else {
        console.log(`Request failed with status: ${res.status}`)
        return null
    }
}

function reservationSeat(token, concertId, concertSeatIdList, userId) {
    let headers = {
        'token': `${token}`,
    };
    let payload = JSON.stringify({
        concertSeatIdList: concertSeatIdList,
        concertId: concertId,
        userId: userId
    });
    let res = http.post(`http://localhost:8081/api/reserve/reserve`, payload, {headers: headers});

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        console.log(`Reservation successful for seat ID: ${concertSeatIdList}`);
    } else {
        console.log(`Reservation failed. Status: ${res.status}`);
    }
}

function selectRandomSeats(seatList) {
    let numberOfSeats = randomIntBetween(1, 4);
    let selectedSeats = [];

    for (let i = 0; i < numberOfSeats; i++) {
        let randomSeat = randomItem(seatList);
        selectedSeats.push(randomSeat);
        seatList = seatList.filter(seat => seat !== randomSeat);
    }

    return selectedSeats;
}

```
</details>


<br>

### 테스트 결과
#### 1-1. 토큰 발급 load test

![image](https://github.com/user-attachments/assets/463808fe-18c6-447b-bfa4-16b329236a0b)

- 요청 응답 시간 `http_req_duration`: 평균 136.68ms / p95: 279.06ms
- 대기 시간 `http_req_waiting`: 평균 136.47ms / p95: 278.93ms
- 결론: 일반적인 부하에서는 안정적인 응답 속도 유지.


#### 1-2. 토큰발급 peak test

![image](https://github.com/user-attachments/assets/d037f16a-bb1c-4e94-888a-5ef4fe325fc0)

1000 건의 트래픽이 순간적으로 몰리는 상황을 가정해서 테스트를 진행한 결과이다.

- 요청 응답 시간 `http_req_duration`: 평균 283.89ms / p95: 424.35ms
- 대기 시간 `http_req_waiting`: 평균 283.73ms / p95: 424.16ms
- 결론: 1000건으로 급증한 트래픽에서도 안정적인 응답 속도 유지


#### 2. 좌석 선점 load test

좌석 선점 시나리오의 경우, 데이터 출력 부분에서 직렬화 오류가 발생해 스크립트 작성 후 테스트로 검증할 수 없었다.   
직렬화 오류 해결 후 재진행해서 보완하도록 한다.


<br>

### 결론
#### 테스트 결과 요약 (토큰발급)
평균 응답 속도(136~283ms)가 빠르고, P95에서도 424ms로 양호한 성능을 확인하였다.   
1000건으로 진행한 peak test 결과에서도 안정적으로 테스트가 수행됨을 확인하였다.

#### 개선 방안
- 최대 부하 처리 시간 개선: 유저 트래픽을 변경해보면서 가장 최적화할 수 있는 환경을 고안하고자 한다.
- 좌석 선점 API 안정화: 직렬화 오류 해결 후 부하테스트를 수행해 최적화 방법을 고안하고자 한다.


#### 향후 계획
- docker 를 통해 배포한 후 추가 부하 테스트 수행해 결과의 신뢰성을 보완한다.
- 좌석 선점 시나리오 부하 테스트를 수행 후, 결과를 토대로 최적화를 진행한다.
- TPS 및 가상 유저수 설정에 대한 근거를 추가한다.
  - 아래 트랜잭션 규모 도출 과정을 통해 현재 애플리케이션에 적절한 테스트규모를 설정한다.
 
<details>
<summary>평균 트랜잭션 도출</summary>

1. 트랜잭션 정의
    - 비슷한 서비스인 인터파크의 MAU가 1700만 - [기사](http://www.dhns.co.kr/news/articleView.html?idxno=312396)
    - 해당 서비스에서 티켓 서비스만 분리해 MAU 800만 추정
    - 트래픽이 순간적으로 몰리지 않는 상황 가정 시 30만 DAU 가질 것이라고 추측
    - 내가 생성한 서비스는 그보다 낮은 10만 DAU를 상정하고 테스트를 수행하도록 함
2. 트래픽 볼륨 정의
    - VUSER: 10,000 user
        - 도출한 DAU의 10% 정도를 실제 동시 접속자로 가정
        - 100,000 * 10% -> 10,000 user
    - 트랜잭션수: 120,000 tx
        - 평균 대기시간 1분일 때, 5초에 한번 폴링으로 토큰 조회 시나리오 진행 시 평균 요청 12회 수행
        - 12 tx * 1만 user -> 120,000 tx
    - TPS: 2,000 tps
        - 분당 트랜잭션 수를 초당으로 수정
        - 120,000 tx / 60 s -> 2,000 tps
3. 테스트 목표 TPS 설정
    - 2,000 TPS * 25% = 500TPS
        - 현재 테스트는 local 환경에서 수행될 것이므로 목표 TPS의 25%를 목표로 함
4. 목표 응답 시간
    - RT가 250ms라고 가정 시 초당 한 사숑자가 처리할 수 있는 트랜잭션 수는 4개
    - 500 tps / 4 tps -> 수용가능한 동시사용자 수 125명

</details>
