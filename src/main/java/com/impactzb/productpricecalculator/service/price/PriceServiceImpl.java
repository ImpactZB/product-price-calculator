package com.impactzb.productpricecalculator.service.price;

import com.impactzb.productpricecalculator.data.dto.PriceDto;
import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import com.impactzb.productpricecalculator.exception.ApiBadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PriceServiceImpl implements PriceService{

    private final ProductRepository productRepository;

    private final Set<PriceCalculatorStrategy> priceWithDiscountCalculatorStrategySet;

    private final PriceWithoutDiscountCalculatorStrategy priceCalculatorNoDiscountStrategy;

    @Override
    public PriceDto getProductPrice(UUID productId, Long amount, DiscountType discountType) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiBadRequestException(String.format("Product with id %s doesn't exists!", productId)));

        BigDecimal price = priceWithDiscountCalculatorStrategySet.stream()
                .filter(strategy -> strategy.canHandle(discountType))
                .findAny()
                .map(strategy -> strategy.calculatePrice(product, amount) )
                .orElseGet(() -> priceCalculatorNoDiscountStrategy.calculatePrice(product, amount));

        return PriceDto.builder()
                .price(price)
                .build();
    }

}
