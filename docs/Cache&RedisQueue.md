## 캐싱 및 Redis 대기열 설계

## Cache
- 데이터를 임시로 복사해두는 Storage 계층
- 적은 부하로 API 응답을 빠르게 처리 가능
- 조회 빈도가 높고 변동이 적은 데이터에 적합

### 캐시 적용 포인트
#### 콘서트 조회 ( 적용 )
- 콘서트 정보는 빈번하게 변경되지 않으므로 캐시를 사용하는 데에 적합하다고 판단
- 목록 조회의 경우 검색 조건이 너무 다양해서 캐싱을 하는 것이 의미가 없고 키를 설정하기 애매하다고 생각해 제외함

#### 콘서트 좌석 조회 ( 미적용 )
- 좌석 정보의 경우 실시간으로 좌석 선점이 일어나기 때문에 변경 빈도가 높아 적절하지 않다고 판단

#### 예약/결제/포인트 ( 미적용 )
- 사용자의 개인정보가 포함되는 부분은 민감할 수 있다고 생각함
- 실시간 정보가 중요하지 않을까 생각해 미적용

### 캐싱 전략
- Redis 사용한 캐싱 적용
  - 콘서트 생성 시 최초 캐시 등록 ( TTL 30min )
  - 콘서트 조회 시 캐시가 없을 경우 등록 및 데이터 반환 ( TTL 30min )

### 성능 비교 

#### K6 부하테스트
[k6 script](../k6/script/concert-cache-test.js)

- 가상 사용자 수: 100 
- 테스트 실행 시간 1 min   

|       항목        |  캐시 사용   |  캐시미사용   |
|:---------------:|:--------:|:--------:|
| **단건 조회 응답 시간** |   6ms    |   17ms   |
|  **평균 응답 시간**   | 8.61 ms  | 12.44 ms |
|  **최소 응답 시간**   |  585 µs  |  729 µs  |
|  **중간값 응답 시간**  | 7.48 ms  | 11.86 ms |
|     **90p**     | 17.31 ms | 22.09 ms |
|     **95p**     | 20.93 ms | 26.2 ms  |
|  **최대 응답 시간**   | 41.63 ms | 59.02 ms |

캐시를 사용한 경우 사용하지 않은 경우보다 빠른 조회 속도를 보장함


<br>


## Redis 대기열
- 기존에 DB를 사용하던 대기열을 Redis 로 이관
- NoSQL 기반이면서 in-memory에서 데이터를 처리하기때문에 메모리 처리 속도 향상

### 적용 포인트
- 기존에 RDB를 사용해 처리하던 대기열을 Redis로 이관
- redis로 이관함으로써 높은 트래픽에서도 빠른 응답 속도와 성능저하를 방지할 수 있음

### 사용한 자료구조

#### Waiting Queue: `Sorted Set`
- score 기준으로 정렬되며 중복 비허용
- 대기열에 입장한 시간을 score 로 저장하여 접속한 순서대로 정렬될 수 있도록 함

#### Active Queue: `Set`
- 중복 비허용
- 전체 서버에 대해 활성 유저 수를 관리하기 위해 각 token 별로 set 생성해 만료시간( TTL 5min ) 설정

### 구현 방식
- 스케줄러를 사용해 30초마다 활성인원 수를 확인한 후 단위수(_ACTIVE_SIZE_)만큼 활성화 처리 진행 
- 대기열에서 활성화열로 이동 시 기존 대기열에서는 제거함

```java
// 대기열 상태 확인
public TokenInfo.Main checkToken(String givenToken) {
    String token = givenToken;
    if (givenToken == null) {
        // 토큰 없을 경우 신규 생성
        token = UUID.randomUUID().toString();

        long activeCount = waitingRepository.getActiveCount();
        if (activeCount < ACTIVE_SIZE) {
            // 대기인원 적을 경우 active
            waitingRepository.addActiveQueue(token);
            Waiting waiting = Waiting.builder()
                    .token(token)
                    .status(WaitingStatus.ACTIVE)
                    .build();
            return TokenInfo.Main.of(waiting);
        }

        // waiting 대기열 추가
        waitingRepository.addWaitingQueue(token);
        return TokenInfo.Main.of(getWaiting(token));
    }

    // 토큰 존재 시 대기열 정보 조회
    return TokenInfo.Main.of(getWaiting(token));
}
```
