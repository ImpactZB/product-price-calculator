package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.model.AmountBasedDiscount;
import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.AmountBasedDiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@AllArgsConstructor
@Component
public class PriceWithAmountBasedDiscountCalculatorStrategy implements PriceCalculatorStrategy {

    private final PriceWithoutDiscountCalculatorStrategy priceWithoutDiscountCalculatorStrategy;

    private final AmountBasedDiscountRepository amountBasedDiscountRepository;

    @Override
    public BigDecimal calculatePrice(Product product, Long amount) {
        return amountBasedDiscountRepository.findByProductId(product.getId()).stream()
                .filter(discount -> discount.getAmount() <= amount)
                .max(Comparator.comparing(AmountBasedDiscount::getAmount))
                .map(discount -> this.calculatePrice(product, amount, discount))
                .orElseGet(() -> priceWithoutDiscountCalculatorStrategy.calculatePrice(product, amount));
    }

    private BigDecimal calculatePrice(Product product, Long amount, AmountBasedDiscount amountBasedDiscount){
        BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(amountBasedDiscount.getPercentage());
        return priceWithoutDiscountCalculatorStrategy.calculatePrice(product, amount)
                .multiply(discountMultiplier)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean canHandle(DiscountType discountType) {
        return DiscountType.AMOUNT.equals(discountType);
    }
}
