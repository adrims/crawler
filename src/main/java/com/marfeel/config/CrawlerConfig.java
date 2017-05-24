package com.marfeel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configuration class for application
 *
 * @author Adrián Martín Sánchez
 */
@EnableAsync
@Configuration
@ComponentScan(basePackages = "com.marfeel")
public class CrawlerConfig {

    private static Integer THREADS_PER_CORE = 5;

    @Bean
    public Executor threadPoolTaskExecutor() {
        int threadAmount = Runtime.getRuntime().availableProcessors() * THREADS_PER_CORE;
        return Executors.newFixedThreadPool(threadAmount);
    }
}
