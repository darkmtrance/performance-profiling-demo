package com.matomaylla.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service1", url = "http://localhost:8080")
public interface Service1Client {
    @GetMapping("/demo.api/service1/{param}")
    String callService1(@PathVariable("param") String param);

    @GetMapping("/demo.api/service1/chained/{param}")
    String callChainedService1(@PathVariable("param") String param);
}
