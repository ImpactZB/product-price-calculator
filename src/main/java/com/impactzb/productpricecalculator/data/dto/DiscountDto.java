package com.impactzb.productpricecalculator.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class DiscountDto implements Serializable {

    private UUID id;

    private String discountType;

    @NotNull(message = "Percentage value is mandatory")
    @Min(value = 0, message = "Percentage value has to be equal or greater than 0 and lower or equal to 100")
    @Max(value = 100, message = "Percentage value has to be equal or greater than 0 and lower or equal to 100")
    private BigDecimal percentage;
}
