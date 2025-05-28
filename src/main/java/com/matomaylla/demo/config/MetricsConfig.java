package com.matomaylla.demo.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig {

    @Value("${spring.application.name:performance-profiling-demo}")
    private String applicationName;

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public MeterRegistry.Config meterRegistryConfig(MeterRegistry meterRegistry) {
        return meterRegistry.config()
                .commonTags("application", applicationName)
                .commonTags("environment", "demo");
    }
}
