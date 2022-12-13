package com.impactzb.productpricecalculator.service;

import com.impactzb.productpricecalculator.data.model.AmountBasedDiscount;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.AmountBasedDiscountRepository;
import com.impactzb.productpricecalculator.service.price.PriceWithAmountBasedDiscountCalculatorStrategy;
import com.impactzb.productpricecalculator.service.price.PriceWithoutDiscountCalculatorStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class PriceWithAmountBasedDiscountCalculatorStrategyTest {

    PriceWithoutDiscountCalculatorStrategy priceWithoutDiscountCalculatorStrategy = Mockito.mock(PriceWithoutDiscountCalculatorStrategy.class);

    AmountBasedDiscountRepository amountBasedDiscountRepository = Mockito.mock(AmountBasedDiscountRepository.class);

    PriceWithAmountBasedDiscountCalculatorStrategy priceWithAmountBasedDiscountCalculatorStrategy = new PriceWithAmountBasedDiscountCalculatorStrategy(priceWithoutDiscountCalculatorStrategy,
            amountBasedDiscountRepository);

    @Test
    void calculatePrice_shouldCalculatePriceCorrectlyForExistingAmountBasedDiscount(){
        //given
        BigDecimal productPrice = BigDecimal.TEN;
        long amount = 51L;
        Product product = Product.builder().price(productPrice).build();
        AmountBasedDiscount amountBasedDiscount1 = AmountBasedDiscount.builder()
                .product(product)
                .percentage(BigDecimal.TEN)
                .amount(10L)
                .build();
        AmountBasedDiscount amountBasedDiscount2 = AmountBasedDiscount.builder()
                .product(product)
                .percentage(BigDecimal.valueOf(20))
                .amount(50L)
                .build();
        AmountBasedDiscount amountBasedDiscount3 = AmountBasedDiscount.builder()
                .product(product)
                .percentage(BigDecimal.valueOf(40))
                .amount(5000L)
                .build();

        when(amountBasedDiscountRepository.findByProductId(any())).thenReturn(Set.of(amountBasedDiscount1, amountBasedDiscount2, amountBasedDiscount3));
        when(priceWithoutDiscountCalculatorStrategy.calculatePrice(any(), any())).thenCallRealMethod();

        //when
        BigDecimal totalPrice = priceWithAmountBasedDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).multiply(BigDecimal.valueOf(0.80)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }

    @Test
    void calculatePrice_shouldCalculatePriceCorrectlyForNotExistingValidAmountBasedDiscount(){
        //given
        BigDecimal productPrice = BigDecimal.TEN;
        long amount = 9L;
        Product product = Product.builder().price(productPrice).build();
        AmountBasedDiscount amountBasedDiscount = AmountBasedDiscount.builder()
                .product(product)
                .percentage(BigDecimal.TEN)
                .amount(10L)
                .build();

        when(amountBasedDiscountRepository.findByProductId(any())).thenReturn(Set.of(amountBasedDiscount));
        when(priceWithoutDiscountCalculatorStrategy.calculatePrice(any(), any())).thenCallRealMethod();

        //when
        BigDecimal totalPrice = priceWithAmountBasedDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }

    @Test
    void calculatePrice_shouldCalculatePriceCorrectlyForNotExistingAmountBasedDiscounts(){
        //given
        BigDecimal productPrice = BigDecimal.TEN;
        long amount = 51;
        Product product = Product.builder().price(productPrice).build();

        when(amountBasedDiscountRepository.findByProductId(any())).thenReturn(Set.of());
        when(priceWithoutDiscountCalculatorStrategy.calculatePrice(any(), any())).thenCallRealMethod();

        //when
        BigDecimal totalPrice = priceWithAmountBasedDiscountCalculatorStrategy.calculatePrice(product, amount);

        //then
        assertEquals(productPrice.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_UP), totalPrice);
    }

}
