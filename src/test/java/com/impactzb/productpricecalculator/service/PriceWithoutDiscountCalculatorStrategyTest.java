package com.impactzb.productpricecalculator.service;

import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.service.price.PriceWithoutDiscountCalculatorStrategy;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PriceWithoutDiscountCalculatorStrategyTest extends PriceCalculationTestData {

    PriceWithoutDiscountCalculatorStrategy priceWithoutDiscountCalculatorStrategy = new PriceWithoutDiscountCalculatorStrategy();

    @ParameterizedTest
    @MethodSource("provideCalculationFactors")
    void calculatePrice_shouldCalculatePriceCorrectly(BigDecimal productPrice, long amount){
        //given
        Product product = Product.builder().price(productPrice).build();

        //when
        BigDecimal totalPrice = priceWithoutDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }


}
