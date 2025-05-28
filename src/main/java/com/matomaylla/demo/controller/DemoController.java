package com.matomaylla.demo.controller;

import com.matomaylla.demo.service.ComplexOperationService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

    private final ComplexOperationService complexOperationService;

    @PostMapping("/login/{username}")
    @Timed(value = "demo.api.login", description = "Time taken for login process")
    public ResponseEntity<String> login(@PathVariable String username) {
        complexOperationService.simulateUserLogin(username);
        return ResponseEntity.ok("Login successful for user: " + username);
    }

    @PostMapping("/logout/{username}")
    @Timed(value = "demo.api.logout", description = "Time taken for logout process")
    public ResponseEntity<String> logout(@PathVariable String username) {
        complexOperationService.simulateUserLogout(username);
        return ResponseEntity.ok("Logout successful for user: " + username);
    }

    @GetMapping("/complex-operation")
    @Timed(value = "demo.api.complex", description = "Time taken for complex operation", percentiles = {0.5, 0.95, 0.99})
    public ResponseEntity<String> complexOperation() {
        String result = complexOperationService.performComplexOperation();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/custom-metric/{name}")
    public ResponseEntity<String> recordCustomMetric(
            @PathVariable String name,
            @RequestParam double value) {
        complexOperationService.recordCustomMetric(name, value);
        return ResponseEntity.ok("Custom metric recorded");
    }
}
