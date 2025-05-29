package com.matomaylla.demo.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo.api/service2")
public class Service2Controller {

    @GetMapping("/{param}")
    @Timed(value = "demo.api.service2", description = "Time taken for service2 operation")
    public ResponseEntity<String> processRequest(@PathVariable String param) throws InterruptedException {
        // Simulate processing time
        TimeUnit.MILLISECONDS.sleep(150);
        return ResponseEntity.ok("Service2 processed: " + param);
    }

    @GetMapping("/chained/{param}")
    @Timed(value = "demo.api.service2.chained", description = "Time taken for service2 chained operation")
    public ResponseEntity<String> chainedRequest(@PathVariable String param) throws InterruptedException {
        // Simulate longer processing time in chain
        TimeUnit.MILLISECONDS.sleep(300);
        return ResponseEntity.ok("Service2 chained: " + param);
    }
}
