## 쿼리 성능 개선
- 성능 개선이 필요한 쿼리를 찾고 개선하여 결과를 정리
- 단순히 PK만으로 조회하는 쿼리는 이미 인덱스를 통해 조회하기 때문에 제외함 (ex. findConcertSeatList)

### 1. 콘서트 목록 조회
- 예약 가능한 콘서트 조회는 가장 빈번하게 사용될 것이라 인덱스 설정 시 큰 효과를 볼 수 있을 것으로 예상
- 데이터 수: 1천만건

#### 기존 쿼리
```sql
select * from concert where date(:date) between date(reservation_start_at) and date(reservation_end_at);
```
- PK 에만 인덱스가 설정되어 테이블을 전체 스캔해 데이터를 조회

| select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows    | filtered | Extra         |
|-------------|---------|------------|------|---------------|------|---------|------|---------|----------|---------------|
| SIMPLE      | concert | null       | ALL  | null          | null | null    | null | 9941647 | 100      | Using where   |
```text
-> Filter: ((<cache>(cast(((2023 - 1) - 1) as date)) >= cast(concert.reservation_start_at as date)) and (<cache>(cast(((2023 - 1) - 1) as date)) <= cast(concert.reservation_end_at as date)))  (cost=1.06e+6 rows=9.94e+6) (actual time=3176..3176 rows=0 loops=1)
    -> Table scan on concert  (cost=1.06e+6 rows=9.94e+6) (actual time=0.17..2946 rows=10e+6 loops=1)
```

#### 인덱스 생성
```sql
CREATE INDEX idx_reservation_start_date ON concert (reservation_start_at);
CREATE INDEX idx_reservation_end_date ON concert (reservation_end_at);
```
- 하지만 MySQL의 DATE 함수를 사용하기때문에 여전히 전체 스캔이 발생함

#### 수정 쿼리
```sql
select * from concert c where c.reservation_start_at <= :endDate and c.reservation_end_at >= :startDate;
```
- `date` 함수를 제거하고 between 연산자 대신 부등호를 사용해 조건을 간소화 함

| select_type | table   | type  | possible_keys                                       | key                        | key_len | rows | filtered | Extra                                         |
|-------------|---------|-------|-----------------------------------------------------|----------------------------|---------|------|----------|-----------------------------------------------|
| SIMPLE      | concert | range | idx_reservation_start_date,idx_reservation_end_date | idx_reservation_start_date | 9       | 640  | 50       | Using index condition; Using where; Using MRR |
```text
-> Filter: (concert.reservation_end_at >= TIMESTAMP'2023-01-01 00:00:00')  (cost=711 rows=320) (actual time=1.12..3.63 rows=620 loops=1)
    -> Index range scan on concert using idx_reservation_start_date over (NULL < reservation_start_at <= '2023-01-01 23:59:59.000000'), with index condition: (concert.reservation_start_at <= TIMESTAMP'2023-01-01 23:59:59')  (cost=711 rows=640) (actual time=0.517..3.33 rows=640 loops=1)
```

- 검색하는 날짜를 해당 날짜의 시작시간, 종료시간으로 분리
  - MySQL의 함수를 사용하지 않도록 해 날짜조건도 인덱스를 탈 수 있도록 수정 
- 수행 결과 인덱스 범위 스캔을 수행

#### 전 후 비교
| 항목               | 인덱스 추가 전           | 인덱스 추가 후        |
|------------------|--------------------|-----------------|
| **쿼리 방식**        | 전체 테이블 스캔          | 인덱스 범위 스캔       |
| **Filter 조건**    | 검색 컬럼에 필터 각각 적용    | 부등호 사용한 조건 간소화  |
| **쿼리 비용 (cost)** | 1.06e+6 (106만)     | 711             |
| **스캔된 행 수**      | 약 9.94e+6 (994만) 행 | 약 640 행         |
| **실제 실행 시간**     | 3176ms             | 1.12ms ~ 3.63ms |
| **추가 최적화**       | 없음                 | 인덱스 범위 조건       |


> MySQL의 함수를 인덱스로 거는 방식도 괜찮을지 확인 필요
> 
> `CREATE INDEX idx_reservation_start_date ON concert (DATE(reservation_start_at));`


<br>


### 2. 유저 포인트(잔액) 조회
- 포인트 조회 시 성능 개선을 위해 `user_id` 컬럼에 인덱스를 생성하면 조회 성능을 높일 수 있을 것으로 예상
- 데이터 수: 1백만건

#### 기존 쿼리
```sql
select * from point where user_id = :id
```
- PK 에만 인덱스가 설정되어 테이블을 전체 스캔해 데이터를 조회

| select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows  | filtered | Extra         |
|-------------|-------|------------|------|---------------|------|---------|------|-------|----------|---------------|
| SIMPLE      | point | null       | ALL  | null          | null | null    | null | 99847 | 10       | Using where   |
```text
-> Filter: (`point`.user_id = 1)  (cost=10073 rows=9985) (actual time=0.0712..38.3 rows=1 loops=1)
    -> Table scan on point  (cost=10073 rows=99847) (actual time=0.0698..33.8 rows=100000 loops=1)
```

#### 인덱스 생성
```sql
CREATE INDEX idx_user_id ON point (user_id);
```
- user_id 인덱스를 설정해 조회 속도 개선

| select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows  | filtered | Extra         |
|-------------|-------|------------|------|---------------|------|---------|------|-------|----------|---------------|
| SIMPLE      | point | null       | ALL  | null          | null | null    | null | 99847 | 10       | Using where   |
```text
-> Index lookup on point using idx_user_id (user_id=1)  (cost=0.35 rows=1) (actual time=0.0317..0.0333 rows=1 loops=1)
```
