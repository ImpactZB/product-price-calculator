package com.impactzb.productpricecalculator.data.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AmountBasedDiscountDto extends DiscountDto {

    @NotNull(message = "Amount value is mandatory")
    @Min(value = 1, message = "Amount value has to be equal or greater than 1")
    @Max(value = Long.MAX_VALUE, message = "Amount value has to be equal or lower than " + Long.MAX_VALUE)
    private Long amount;
}
