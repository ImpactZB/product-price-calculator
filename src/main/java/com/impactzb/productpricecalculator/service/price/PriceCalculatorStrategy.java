package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.Product;

import java.math.BigDecimal;

public interface PriceCalculatorStrategy {

    BigDecimal calculatePrice(Product product, Long amount);

    boolean canHandle(DiscountType discountType);
}
