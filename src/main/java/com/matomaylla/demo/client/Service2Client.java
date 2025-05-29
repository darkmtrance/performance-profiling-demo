package com.matomaylla.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service2", url = "http://localhost:8080")
public interface Service2Client {
    @GetMapping("/demo.api/service2/{param}")
    String callService2(@PathVariable("param") String param);

    @GetMapping("/demo.api/service2/chained/{param}")
    String callChainedService2(@PathVariable("param") String param);
}
