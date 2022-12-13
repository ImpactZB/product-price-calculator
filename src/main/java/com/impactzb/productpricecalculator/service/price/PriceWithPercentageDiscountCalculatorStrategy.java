package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.PercentageBasedDiscount;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.PercentageBasedDiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@Component
public class PriceWithPercentageDiscountCalculatorStrategy implements PriceCalculatorStrategy {

    private final PriceWithoutDiscountCalculatorStrategy priceWithoutDiscountCalculatorStrategy;

    private final PercentageBasedDiscountRepository percentageBasedDiscountRepository;

    @Override
    public BigDecimal calculatePrice(Product product, Long amount) {
        return percentageBasedDiscountRepository.findByProductId(product.getId())
                .map(discount -> this.calculatePrice(product, amount, discount))
                .orElseGet(() -> priceWithoutDiscountCalculatorStrategy.calculatePrice(product, amount));
    }

    private BigDecimal calculatePrice(Product product, Long amount, PercentageBasedDiscount percentageBasedDiscount){
        BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(percentageBasedDiscount.getPercentage());
        return priceWithoutDiscountCalculatorStrategy.calculatePrice(product, amount)
                .multiply(discountMultiplier)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean canHandle(DiscountType discountType) {
        return DiscountType.PERCENTAGE.equals(discountType);
    }
}
