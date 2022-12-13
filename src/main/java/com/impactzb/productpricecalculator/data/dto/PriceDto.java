package com.impactzb.productpricecalculator.data.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PriceDto {
    private BigDecimal price;
}
