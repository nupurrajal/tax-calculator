package com.org.taxcalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.org.taxcalculator.controller","com.org.taxcalculator.service","com.org.taxcalculator.utility"})
@EntityScan("com.org.taxcalculator.model")
public class TaxCalculatorApplication {

    private static final Logger logger = LoggerFactory.getLogger(TaxCalculatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaxCalculatorApplication.class, args);
        logger.info("Tax calculator application started successfully");
    }
}
