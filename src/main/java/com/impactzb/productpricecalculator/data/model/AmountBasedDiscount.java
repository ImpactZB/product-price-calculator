package com.impactzb.productpricecalculator.data.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name="amount_based_discount")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(AmountBasedDiscount.DISCRIMINATOR)
@EqualsAndHashCode(callSuper = true)
public class AmountBasedDiscount extends Discount {

    public static final String DISCRIMINATOR = "AMOUNT";

    @Column(name="amount")
    private Long amount;

    @Column(name="percentage")
    private BigDecimal percentage;

    @Builder
    public AmountBasedDiscount(Product product, Long amount, BigDecimal percentage){
        super(product);
        this.amount = amount;
        this.percentage = percentage;
    }
}
