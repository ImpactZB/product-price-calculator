package com.impactzb.productpricecalculator.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name="product")
@Schema(description = "Product")
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends UuidAbstractPersistable implements Serializable {

    @Column(name="price")
    private BigDecimal price;
}
