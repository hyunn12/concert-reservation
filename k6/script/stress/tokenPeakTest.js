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
