package com.matomaylla.demo.service;

import com.matomaylla.demo.client.Service1Client;
import com.matomaylla.demo.client.Service2Client;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DistributedDemoService {

    private final Service1Client service1Client;
    private final Service2Client service2Client;
    private final WebClient.Builder webClientBuilder;

    @Timed(value = "demo.api.sequential.calls", description = "Time taken for sequential service calls")
    public String demonstrateSequentialCalls(String param) {
        // Anti-pattern: Sequential calls
        String result1 = service1Client.callService1(param);
        String result2 = service2Client.callService2(result1);
        return "Sequential results: " + result1 + " -> " + result2;
    }

    @Timed(value = "demo.api.parallel.calls", description = "Time taken for parallel service calls")
    public String demonstrateParallelCalls(String param) throws ExecutionException, InterruptedException {
        // Good practice: Parallel calls
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> 
            service1Client.callService1(param)
        );
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> 
            service2Client.callService2(param)
        );
        
        return "Parallel results: " + future1.get() + " & " + future2.get();
    }

    @Timed(value = "demo.api.chained.calls", description = "Time taken for chained service calls")
    public String demonstrateChainedCalls(String param) {
        // Anti-pattern: Long chain of calls
        String result1 = service1Client.callChainedService1(param);
        String result2 = service2Client.callChainedService2(result1);
        return "Chained results: " + result1 + " -> " + result2;
    }

    @Timed(value = "demo.api.reactive.calls", description = "Time taken for reactive service calls")
    public String demonstrateReactiveCalls(String param) {
        // Good practice: Using WebClient for reactive calls
        return webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/demo.api/service1/{param}", param)
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(result1 -> 
                webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/demo.api/service2/{param}", result1)
                    .retrieve()
                    .bodyToMono(String.class)
            )
            .block();
    }
}
