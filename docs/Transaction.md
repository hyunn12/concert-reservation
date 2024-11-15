## 트랜잭션 범위 분리
- Facade Layer 의 결제 로직을 트랜잭션 범위에 따라 분리

### 결제 로직
```text
pay {
    좌석_선점시간_조회();
    포인트_차감();
    예약_내역_등록();
    결제_내역_등록();
    좌석_상태_변경();

    토큰_삭제();

    예약_내역_전송();
}
```
- `PaymentFacade` 클래스의 결제 로직은 여러 도메인 서비스를 참조
- 하나의 서비스가 지연되거나 실패하는 경우 전체 결제 프로세스에 영향을 줄 수 있음
- `토큰 삭제` 및 `예약 내역 전송` 의 경우, 결제 성공여부에 영향을 주는 주요로직이 아니기 때문에 분리 필요
- **이벤트 기반 처리**를 통해 주요 로직에 미치는 영향을 줄이고 성능을 보장하고자함


### 트랜잭션 분리 설계
- 기존 로직은 하나의 트랜잭션 내에서 모든 로직 처리
- 부가작업에 대해 주요로직과 트랜잭션을 분리하도록 처리
- 비동기 처리를 통해 독립적인 실행을 보장
```text
결제_tx {
    좌석_선점시간_조회();
    포인트_차감();
    예약_내역_등록();
    결제_내역_등록();
    좌석_상태_변경();
}

토큰_tx {
    토큰_삭제();
}

전송_tx {
    예약_내역_전송();
}
```
- 결제 tx: 결제 주요로직 처리
- 토큰 tx: 토큰 삭제 작업을 비동기적으로 처리
- 전송 tx: 예약내역을 비동기적으로 전송하도록 처리


### 서비스 분리 시 고려사항
- 예외처리 어려움
    - 만약 다른 마이크로 서비스에서 문제가 발생하게 된다면 그 전 로직에서 처리된 커밋을 롤백하기 어려위짐
- 트랜잭션 일관성 깨짐
  - 각각의 마이크로 서비스에서 독립된 DB를 사용할 경우 하나의 트랜잭션으로 관리하기 어려움
  - 데이터 일관성을 유지하기 어려워짐

### 해결 방안
앞서 서술한 일련의 문제점으로 인해 MSA 형태로 구성된 서비스에서는 주로 분산 트랜잭션을 사용해 서비스 구현

#### 분산 트랜잭션의 문제점
트랜잭션을 분리해도 아래에 정리한 문제 외에도 여러 문제가 발생할 수 있어 적절한 처리가 필요

- 데이터 일관성 문제
  - 트랜잭션의 범위가 분리되면서 각 트랜잭션이 독립적으로 커밋되거나 롤백됨
  - 하나는 성공하고 하나는 실패한다면 데이터의 일관성이 깨질 수 있음
- 네트워크 지연
  - 네트워크를 통해 다른 서비스와 연동하는 경우가 많아 성능 저하가 일어날 수 있음
- 데드락
  - 여러 노드에서 서로의 데이터에 동시에 접근할 경우, 대기상태에 빠져서 진행이 불가능한 현상

#### 구현 방법
- Spring Event
  - 이벤트 기반 비동기 처리 통해 트랜잭션 범위 분리 및 비동기 실행 가능
- Saga Pattern
  - Choreography Saga
    - 각 서비스가 이벤트 기반으로 트랜잭션 처리하고 필요한 경우 다른 서비스로 이벤트를 전달
  - Orchestration Saga
    - 중앙의 오케스트레이터 서비스가 전체 트랜잭션 흐름을 관리

| 특징              | Orchestration Saga         | Choreography Saga     |
|-----------------|----------------------------|-----------------------|
| **이벤트 생성**      | Orchestrator               | 각 서비스                 |
| **이벤트 수신 및 처리** | 각 서비스가 Orchestrator 이벤트 수신 | 각 서비스가 다른 서비스의 이벤트 구독 |
| **흐름 제어**       | Orchestrator 가 중앙제어        | 각 서비스가 이벤트 생성 및 전달    |
-> 필요한 경우 보상 트랜잭션을 이용해 이전 단계의 트랜잭션을 롤백할 수 있음

- Outbox Pattern
  - 트랜잭션 내 이벤트 데이터를 `outbox`에 저장한 후, 트랜잭션 커밋 후 별도로 다른 서비스로 이벤트를 전송

### 적용
- 현재는 MSA 구조가 아니기 때문에, Spring Event 를 사용해 이벤트 기반 비동기 처리를 통해 트랜잭션 범위를 분리

1. 기존 결제 로직에서 부가작업을 독립적으로 이벤트로 분리해, 메인 결제 로직에 영향을 주지 않도록 함
```java
// PaymentFacade 내 결제 로직에서 이벤트 발생
PaymentSuccessEvent successEvent 
        = new PaymentSuccessEvent(this, payment, reservation, command.getToken());
paymentEventPublisher.success(successEvent);
```

2. 메인 로직과 별개의 트랜잭션으로 처리하기 위해서 `TransactionPhase.AFTER_COMMIT` 설정을 사용   
결제 트랜잭션이 성공하고 커밋된 후에 부가작업이 비동기적으로 실행되도록 함
```java
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final WaitingService waitingService;
    private final ExternalService externalService;

    // 토큰 삭제
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void handleRemoveToken(PaymentSuccessEvent event) {
        waitingService.removeActiveToken(event.getToken());
    }

    // 예약 내역 전송
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void handleNotifyPayment(PaymentSuccessEvent event) {
        externalService.notifyPaymentSuccess(event.getInfo());
    }
}
```
