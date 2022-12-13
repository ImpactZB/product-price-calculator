package com.impactzb.productpricecalculator.data.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name="percentage_based_discount")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(PercentageBasedDiscount.DISCRIMINATOR)
@EqualsAndHashCode(callSuper = true)
public class PercentageBasedDiscount extends Discount {

    public static final String DISCRIMINATOR = "PERCENTAGE";

    @Column(name="percentage")
    BigDecimal percentage;

    @Builder
    public PercentageBasedDiscount(Product product, BigDecimal percentage){
        super(product);
        this.percentage = percentage;
    }
}
