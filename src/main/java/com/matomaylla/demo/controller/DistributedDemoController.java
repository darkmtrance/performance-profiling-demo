package com.matomaylla.demo.controller;

import com.matomaylla.demo.service.DistributedDemoService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/demo.api/distributed")
@RequiredArgsConstructor
public class DistributedDemoController {

    private final DistributedDemoService distributedDemoService;

    @GetMapping("/sequential/{param}")
    @Timed(value = "demo.api.distributed.sequential", description = "Time taken for sequential pattern demo")
    public ResponseEntity<String> sequentialDemo(@PathVariable String param) {
        return ResponseEntity.ok(distributedDemoService.demonstrateSequentialCalls(param));
    }

    @GetMapping("/parallel/{param}")
    @Timed(value = "demo.api.distributed.parallel", description = "Time taken for parallel pattern demo")
    public ResponseEntity<String> parallelDemo(@PathVariable String param) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(distributedDemoService.demonstrateParallelCalls(param));
    }

    @GetMapping("/chained/{param}")
    @Timed(value = "demo.api.distributed.chained", description = "Time taken for chained pattern demo")
    public ResponseEntity<String> chainedDemo(@PathVariable String param) {
        return ResponseEntity.ok(distributedDemoService.demonstrateChainedCalls(param));
    }

    @GetMapping("/reactive/{param}")
    @Timed(value = "demo.api.distributed.reactive", description = "Time taken for reactive pattern demo")
    public ResponseEntity<String> reactiveDemo(@PathVariable String param) {
        return ResponseEntity.ok(distributedDemoService.demonstrateReactiveCalls(param));
    }

    @GetMapping("/simulate")
    @Timed(value = "demo.api.distributed.simulation", description = "Time taken for full simulation")
    public ResponseEntity<String> runFullSimulation() throws ExecutionException, InterruptedException {
        StringBuilder result = new StringBuilder();
        result.append("Sequential call: ").append(distributedDemoService.demonstrateSequentialCalls("test")).append("\n");
        result.append("Parallel call: ").append(distributedDemoService.demonstrateParallelCalls("test")).append("\n");
        result.append("Chained call: ").append(distributedDemoService.demonstrateChainedCalls("test")).append("\n");
        result.append("Reactive call: ").append(distributedDemoService.demonstrateReactiveCalls("test"));
        return ResponseEntity.ok(result.toString());
    }
}
