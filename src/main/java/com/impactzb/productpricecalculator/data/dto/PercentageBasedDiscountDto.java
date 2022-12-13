package com.impactzb.productpricecalculator.data.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PercentageBasedDiscountDto extends DiscountDto {
}
