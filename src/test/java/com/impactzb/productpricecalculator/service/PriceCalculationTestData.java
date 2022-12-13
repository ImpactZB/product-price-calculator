package com.impactzb.productpricecalculator.service;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

public abstract class PriceCalculationTestData {
    protected static Stream<Arguments> provideCalculationFactors() {
        return Stream.of(
                Arguments.of(BigDecimal.TEN, 15L),
                Arguments.of(BigDecimal.valueOf(17.34), 7),
                Arguments.of(BigDecimal.valueOf(99.99), 123333),
                Arguments.of(BigDecimal.valueOf(0.01), 233),
                Arguments.of(BigDecimal.valueOf(333), 1)
        );
    }
}
