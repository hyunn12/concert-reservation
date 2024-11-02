## 동시성 문제 발생 시나리오
- 좌석 선점
- 포인트(잔액) 충전 / 사용


---


## 동시성 제어 방식
### 낙관적락 ( Optimistic Lock ) 
- 데이터 변경 전 충돌여부를 검증하고 재시도
- 충돌 빈도가 낮고 트랜잭션이 짧은 경우 적합
- DB에 직접 lock을 걸지 않아 성능 부담이 낮음
- 단점 : 충돌이 자주 발생할 경우 성능 저하

### 비관적락 ( Pessimistic Lock )
- 데이터 접근 시 다른 트랜잭션이 접근하지 못하게 선점해 락 유지
- 충돌 빈도가 높고 트랜잭션이 긴 경우 적합
- lock을 점유해 충돌 위험이 낮음
- 단점 : 대기 시간이 길어지거나 데드락 우려가 있음

### 분산락
- 분산 환경에서 동시성 제어가 필요한 경우 적합
- 빠른 락 설정, 해제 가능
- 단점 : Redis 서버 의존도가 높음


---


## 상황별 제어 방식 선정

### 1. 좌석 선점

#### 상황
- 한 명의 유저가 여러 개의 좌석에 동시에 접근 할 수 있음
- 한 개의 좌석에 여러 명의 유저가 동시에 접근 시도할 수 있음
- 한 명의 유저가 좌석을 점유할 경우, 다른 유저들은 순서에 상관없이 모두 실패해야함
- 재시도 필요 없음

#### 결론
- 낙관적락이 적합하다고 판단 
```java
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT cs FROM ConcertSeat cs WHERE cs.seatId IN :seatIdList")
    List<ConcertSeat> findConcertSeatListWithLock(@Param("seatIdList") List<Long> seatIdList);
```

#### `만약 대용량 트래픽이 발생하는 환경이라면?`
- 충돌이 빈번하게 발생할 수 있으므로 redis 를 활용한 분산락을 적용
- 단순히 한 명의 요청만 성공 후 나머지는 빠르게 실패처리를 해야하므로
분산락 중 simple lock 으로도 구현할 수 있을 것으로 판단

#### 테스트 시간 측정
- 테스트 상황: `threadCount: 10` / `requestCount: 1000` 
    - 비관적락 적용 시 / 1148 ms   
        <img width="500" alt="image" src="https://github.com/user-attachments/assets/38f710bd-d1d9-41e9-b378-34a096dffb11">
    - 낙관적락 적용 시 / 840 ms   
        <img width="500" alt="image" src="https://github.com/user-attachments/assets/a5ef86ac-2fb9-42bc-bd83-4c43c41c948e">


### 2. 포인트(잔액) 충전 / 사용

#### 상황
- 한 명의 유저가 본인의 포인트에 동시에 접근할 수 있음
- 하나의 트랜잭션이 먼저 점유한 경우 나머지는 해당 점유가 끝날 때까지 대기해야함
- 재시도 필요
- 다른 사람의 포인트에는 접근할 수 없고 본인의 포인트에만 접근하므로 대기가 어느정도 있어도 괜찮을 것 같다는 생각
- 상대적으로 충돌이 빈번하게 일어나지도 않을 것 같음

#### 결론
- 비관적락이 적합하다고 판단함
- 만일 대용량 트래픽 환경이어도 분산락까지 적용할 정도로 과도한 트래픽이 발생할 것 같지않음
```java
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Point p where p.userId = :userId")
    Optional<Point> findByUserIdWithLock(@Param("userId") Long userId);
```

#### 테스트 시간 측정
- 테스트 상황: `threadCount: 10` / `requestCount: 10`
    - 비관적락 적용 시 / 53 ms   
        <img width="500" alt="image" src="https://github.com/user-attachments/assets/cd3e2e73-a083-4888-8d7d-fd58b5b4d032">
    - 낙관적락 적용 시 / 110 ms   
        <img width="500" alt="image" src="https://github.com/user-attachments/assets/e950f827-567f-4051-81c4-83ee5cc6b333">

