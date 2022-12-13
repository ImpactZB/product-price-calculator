package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.PercentageBasedDiscountDto;
import com.impactzb.productpricecalculator.data.mapper.DiscountMapper;
import com.impactzb.productpricecalculator.data.repository.PercentageBasedDiscountRepository;
import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import com.impactzb.productpricecalculator.exception.ApiBadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PercentageBasedDiscountServiceImpl implements PercentageBasedDiscountService{

    private final ProductRepository productRepository;

    private final PercentageBasedDiscountRepository percentageBasedDiscountRepository;

    private final DiscountMapper discountMapper;

    @Override
    @Transactional
    public PercentageBasedDiscountDto createPercentageBasedDiscount(UUID productId, PercentageBasedDiscountDto discountDto) {
        validateDiscount(discountDto.getPercentage(), productId);
        percentageBasedDiscountRepository.findByProductId(productId).ifPresent(percentageBasedDiscountRepository::delete);
        return productRepository.findById(productId)
                .map(product -> discountMapper.toPercentageBasedDiscount(discountDto, product))
                .map(percentageBasedDiscountRepository::save)
                .map(discountMapper::toPercentageBasedDiscountDto)
                .orElseThrow(() -> new ApiBadRequestException(String.format("Product with id %s doesn't exists!", productId)));
    }

    private void validateDiscount(BigDecimal percentage, UUID productId){
        percentageBasedDiscountRepository.findByPercentageAndProductId(percentage, productId)
                .ifPresent(discount -> {throw new ApiBadRequestException("Exactly same product discount already exists");});
    }
}
