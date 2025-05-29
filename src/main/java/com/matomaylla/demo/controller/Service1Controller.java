package com.matomaylla.demo.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo.api/service1")
public class Service1Controller {

    @GetMapping("/{param}")
    @Timed(value = "demo.api.service1", description = "Time taken for service1 operation")
    public ResponseEntity<String> processRequest(@PathVariable String param) throws InterruptedException {
        // Simulate processing time
        TimeUnit.MILLISECONDS.sleep(100);
        return ResponseEntity.ok("Service1 processed: " + param);
    }

    @GetMapping("/chained/{param}")
    @Timed(value = "demo.api.service1.chained", description = "Time taken for service1 chained operation")
    public ResponseEntity<String> chainedRequest(@PathVariable String param) throws InterruptedException {
        // Simulate longer processing time in chain
        TimeUnit.MILLISECONDS.sleep(200);
        return ResponseEntity.ok("Service1 chained: " + param);
    }
}
