package com.impactzb.productpricecalculator.data.repository;

import com.impactzb.productpricecalculator.data.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    Set<Discount> findByProductId(UUID productId);
}
