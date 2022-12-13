package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PriceWithoutDiscountCalculatorStrategy implements PriceCalculatorStrategy {

    @Override
    public BigDecimal calculatePrice(Product product, Long amount) {
        return product.getPrice().multiply(new BigDecimal(amount))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean canHandle(DiscountType discountType) {
        return discountType == null;
    }
}
