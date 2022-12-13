package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.DiscountDto;
import com.impactzb.productpricecalculator.data.mapper.DiscountMapper;
import com.impactzb.productpricecalculator.data.model.Discount;
import com.impactzb.productpricecalculator.data.repository.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    @Override
    public Set<Discount> getProductDiscountSet(UUID productId) {
        return discountRepository.findByProductId(productId);
    }

    @Override
    public Set<DiscountDto> getProductDiscountDtoSet(UUID productId) {
        return getProductDiscountSet(productId).stream()
                .map(discountMapper::toDiscountDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteDiscount(UUID discountId) {
        discountRepository.findById(discountId).ifPresent(discountRepository::delete);
    }
}
