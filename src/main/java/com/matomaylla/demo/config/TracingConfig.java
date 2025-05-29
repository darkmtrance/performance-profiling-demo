package com.matomaylla.demo.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.observation.ObservationPredicate;

@Configuration
public class TracingConfig {    @Bean
    public HttpExchangeRepository httpTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }@Bean
    public ObservationPredicate skipActuatorObservation() {
        return (name, context) -> 
            context.getContextualName() == null || 
            !(context.getContextualName().contains("/actuator/prometheus") || 
              context.getContextualName().contains("/actuator/metrics"));
    }
}
