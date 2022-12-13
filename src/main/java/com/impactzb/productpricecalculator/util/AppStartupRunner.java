package com.impactzb.productpricecalculator.util;

import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {

    @Autowired
    ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Service has created following products during the startup:");
        productRepository.findAll()
                .forEach(product -> log.info(String.format("WITH IDS: %s and price %s", product.getId(), product.getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString())));
        log.info("----------------------------------------------------------------------------------------");
        log.info("Finished service initialization");
    }
}
