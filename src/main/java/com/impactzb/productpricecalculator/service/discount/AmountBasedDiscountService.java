package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.AmountBasedDiscountDto;

import java.util.UUID;

public interface AmountBasedDiscountService {
    AmountBasedDiscountDto createAmountBasedDiscount(UUID productId, AmountBasedDiscountDto discountDto);
}
