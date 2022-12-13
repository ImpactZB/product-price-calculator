package com.impactzb.productpricecalculator.data.mapper;

import com.impactzb.productpricecalculator.data.dto.AmountBasedDiscountDto;
import com.impactzb.productpricecalculator.data.dto.DiscountDto;
import com.impactzb.productpricecalculator.data.dto.PercentageBasedDiscountDto;
import com.impactzb.productpricecalculator.data.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel="spring")
public interface DiscountMapper {

    default DiscountDto toDiscountDto(Discount discount){
        if(discount instanceof PercentageBasedDiscount){
            return toPercentageBasedDiscountDto((PercentageBasedDiscount) discount);
        } else if (discount instanceof AmountBasedDiscount) {
            return toAmountBasedDiscountDto((AmountBasedDiscount) discount);
        } else {
            return null;
        }
    }

    @Mapping(source = "percentageBasedDiscount", target = "id", qualifiedByName = "idMapper")
    @Mapping(target = "discountType", constant = PercentageBasedDiscount.DISCRIMINATOR)
    PercentageBasedDiscountDto toPercentageBasedDiscountDto(PercentageBasedDiscount percentageBasedDiscount);

    @Mapping(source = "amountBasedDiscount", target = "id", qualifiedByName = "idMapper")
    @Mapping(target = "discountType", constant = AmountBasedDiscount.DISCRIMINATOR)
    AmountBasedDiscountDto toAmountBasedDiscountDto(AmountBasedDiscount amountBasedDiscount);

    @Named("idMapper")
    default UUID idMapper(Discount discount){
        return discount.getId();
    }

    @Mapping(source = "product", target = "product")
    PercentageBasedDiscount toPercentageBasedDiscount(PercentageBasedDiscountDto percentageBasedDiscountDto, Product product);

    @Mapping(source = "product", target = "product")
    AmountBasedDiscount toAmountBasedDiscount(AmountBasedDiscountDto amountBasedDiscountDto, Product product);
}
