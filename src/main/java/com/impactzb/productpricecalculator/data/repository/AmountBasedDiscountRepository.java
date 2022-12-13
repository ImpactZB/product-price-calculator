package com.impactzb.productpricecalculator.data.repository;

import com.impactzb.productpricecalculator.data.model.AmountBasedDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AmountBasedDiscountRepository extends JpaRepository<AmountBasedDiscount, UUID> {

    Set<AmountBasedDiscount> findByProductId(UUID productId);
    Optional<AmountBasedDiscount> findByPercentageAndAmountAndProductId(BigDecimal percentage, Long amount, UUID productId);

}
