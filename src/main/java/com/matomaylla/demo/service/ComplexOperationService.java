package com.matomaylla.demo.service;

import io.micrometer.core.instrument.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ComplexOperationService {
    
    private final MeterRegistry meterRegistry;
    private final Random random;
    private final AtomicInteger activeUsers;
    private final Gauge activeUsersGauge;
    private final Counter failedLoginAttempts;
    private final Timer complexOperationTimer;

    public ComplexOperationService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.random = new Random();
        this.activeUsers = new AtomicInteger(0);
        
        // Initialize metrics
        this.activeUsersGauge = Gauge
            .builder("demo.users.active", this.activeUsers, AtomicInteger::get)
            .description("Number of active users")
            .register(meterRegistry);

        this.failedLoginAttempts = Counter
            .builder("demo.login.failures")
            .description("Number of failed login attempts")
            .register(meterRegistry);

        this.complexOperationTimer = Timer
            .builder("demo.operation.complex")
            .description("Timer for complex operations")
            .publishPercentiles(0.5, 0.95, 0.99)
            .publishPercentileHistogram()
            .register(meterRegistry);
    }

    public void simulateUserLogin(String username) {
        try {
            // Simulate some processing
            Thread.sleep(random.nextInt(100));
            
            if (random.nextDouble() < 0.1) { // 10% chance of failure
                failedLoginAttempts.increment();
                throw new RuntimeException("Login failed for user: " + username);
            }
            
            activeUsers.incrementAndGet();
            log.info("User {} logged in successfully", username);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void simulateUserLogout(String username) {
        activeUsers.decrementAndGet();
        log.info("User {} logged out", username);
    }

    public String performComplexOperation() {
        return complexOperationTimer.record(() -> {
            try {
                // Simulate complex processing
                Thread.sleep(random.nextInt(500));
                
                if (random.nextDouble() < 0.05) { // 5% chance of error
                    throw new RuntimeException("Complex operation failed");
                }
                
                return "Operation completed successfully";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Operation interrupted";
            }
        });
    }

    public void recordCustomMetric(String metricName, double value) {
        meterRegistry.gauge("demo.custom." + metricName, value);
    }
}
