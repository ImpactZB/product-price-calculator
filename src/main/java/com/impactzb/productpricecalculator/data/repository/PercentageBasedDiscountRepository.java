package com.impactzb.productpricecalculator.data.repository;

import com.impactzb.productpricecalculator.data.model.PercentageBasedDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PercentageBasedDiscountRepository extends JpaRepository<PercentageBasedDiscount, UUID> {

    Optional<PercentageBasedDiscount> findByProductId(UUID productId);

    Optional<PercentageBasedDiscount> findByPercentageAndProductId(BigDecimal percentage, UUID productId);
}
