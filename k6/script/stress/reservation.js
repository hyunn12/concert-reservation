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
