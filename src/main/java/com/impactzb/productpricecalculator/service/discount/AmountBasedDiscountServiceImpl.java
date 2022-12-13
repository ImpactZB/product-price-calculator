package com.impactzb.productpricecalculator.service.discount;

import com.impactzb.productpricecalculator.data.dto.AmountBasedDiscountDto;
import com.impactzb.productpricecalculator.data.mapper.DiscountMapper;
import com.impactzb.productpricecalculator.data.repository.AmountBasedDiscountRepository;
import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import com.impactzb.productpricecalculator.exception.ApiBadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AmountBasedDiscountServiceImpl implements AmountBasedDiscountService {

    private final ProductRepository productRepository;

    private final AmountBasedDiscountRepository amountBasedDiscountRepository;

    private final DiscountMapper discountMapper;

    @Override
    @Transactional
    public AmountBasedDiscountDto createAmountBasedDiscount(UUID productId, AmountBasedDiscountDto discountDto) {
        validateDiscount(productId, discountDto);
        return productRepository.findById(productId)
                .map(product -> discountMapper.toAmountBasedDiscount(discountDto, product))
                .map(amountBasedDiscountRepository::save)
                .map(discountMapper::toAmountBasedDiscountDto)
                .orElseThrow(() -> new ApiBadRequestException(String.format("Product with id %s doesn't exists!", productId)));
    }

    private void validateDiscount(UUID productId, AmountBasedDiscountDto discountDto){
        amountBasedDiscountRepository.findByPercentageAndAmountAndProductId(discountDto.getPercentage(), discountDto.getAmount(), productId)
                .ifPresent(discount -> {throw new ApiBadRequestException("Exactly same product discount already exists");});
    }
}
