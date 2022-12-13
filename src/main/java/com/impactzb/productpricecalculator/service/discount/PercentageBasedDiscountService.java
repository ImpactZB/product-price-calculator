package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.PercentageBasedDiscountDto;

import java.util.UUID;

public interface PercentageBasedDiscountService {
    PercentageBasedDiscountDto createPercentageBasedDiscount(UUID productId, PercentageBasedDiscountDto discountDto);
}
