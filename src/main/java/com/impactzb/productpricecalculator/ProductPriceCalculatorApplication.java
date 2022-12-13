package com.impactzb.productpricecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ProductPriceCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductPriceCalculatorApplication.class, args);
	}

}
