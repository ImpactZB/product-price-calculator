package com.impactzb.productpricecalculator.service;

import com.impactzb.productpricecalculator.data.model.PercentageBasedDiscount;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.PercentageBasedDiscountRepository;
import com.impactzb.productpricecalculator.service.price.PriceWithPercentageDiscountCalculatorStrategy;
import com.impactzb.productpricecalculator.service.price.PriceWithoutDiscountCalculatorStrategy;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceWithPercentageDiscountCalculatorStrategyTest extends PriceCalculationTestData {

    PriceWithoutDiscountCalculatorStrategy priceWithoutDiscountCalculatorStrategy = Mockito.mock(PriceWithoutDiscountCalculatorStrategy.class);

    PercentageBasedDiscountRepository percentageBasedDiscountRepository = Mockito.mock(PercentageBasedDiscountRepository.class);

    PriceWithPercentageDiscountCalculatorStrategy priceWithPercentageDiscountCalculatorStrategy = new PriceWithPercentageDiscountCalculatorStrategy(priceWithoutDiscountCalculatorStrategy,
            percentageBasedDiscountRepository);

    @ParameterizedTest
    @MethodSource("provideCalculationFactors")
    void calculatePrice_shouldCalculatePriceCorrectly(BigDecimal productPrice, long amount){
        //given
        Product product = Product.builder().price(productPrice).build();
        BigDecimal discountPercentage = BigDecimal.TEN;

        PercentageBasedDiscount percentageBasedDiscount = PercentageBasedDiscount.builder()
                .product(product)
                .percentage(discountPercentage)
                .build();

        when(percentageBasedDiscountRepository.findByProductId(any())).thenReturn(Optional.of(percentageBasedDiscount));
        when(priceWithoutDiscountCalculatorStrategy.calculatePrice(any(), any())).thenCallRealMethod();

        //when
        BigDecimal totalPrice = priceWithPercentageDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).multiply(BigDecimal.valueOf(0.90)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }

    @ParameterizedTest
    @MethodSource("provideCalculationFactors")
    void calculatePrice_shouldCalculatePriceCorrectlyForNotExistingDiscount(BigDecimal productPrice, long amount){
        //given
        Product product = Product.builder().price(productPrice).build();

        when(percentageBasedDiscountRepository.findByProductId(any())).thenReturn(Optional.empty());
        when(priceWithoutDiscountCalculatorStrategy.calculatePrice(any(), any())).thenCallRealMethod();

        //when
        BigDecimal totalPrice = priceWithPercentageDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }

}