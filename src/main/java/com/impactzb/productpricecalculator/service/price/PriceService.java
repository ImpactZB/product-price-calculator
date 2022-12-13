package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.dto.PriceDto;
import com.impactzb.productpricecalculator.data.model.DiscountType;

import java.util.UUID;

public interface PriceService {
    PriceDto getProductPrice(UUID productId, Long amount, DiscountType discountType);
}
