package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.DiscountDto;
import com.impactzb.productpricecalculator.data.model.Discount;

import java.util.Set;
import java.util.UUID;

public interface DiscountService {

    Set<Discount> getProductDiscountSet(UUID productId);
    Set<DiscountDto> getProductDiscountDtoSet(UUID productId);

    void deleteDiscount(UUID discountId);
}
