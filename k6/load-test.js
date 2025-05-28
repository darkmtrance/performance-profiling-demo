import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },  // Ramp up to 10 users over 30 seconds
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        http_req_failed: ['rate<0.1'],    // Less than 10% of requests should fail
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    // Simulate user login
    const loginRes = http.post(`${BASE_URL}/api/login/user${__VU}`);
    check(loginRes, {
        'login status is 200': (r) => r.status === 200,
    });

    sleep(1);

    // Execute complex operation
    const complexOpRes = http.get(`${BASE_URL}/api/complex-operation`);
    check(complexOpRes, {
        'complex operation status is 200': (r) => r.status === 200,
    });

    sleep(2);

    // Simulate user logout
    const logoutRes = http.post(`${BASE_URL}/api/logout/user${__VU}`);
    check(logoutRes, {
        'logout status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
