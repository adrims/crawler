package com.marfeel.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for test
 *
 * @author Adrián Martín Sánchez
 */
@Configuration
@ComponentScan(basePackages = {"com.marfeel.crawler","com.marfeel.site"})
public class TestConfiguration {

}
