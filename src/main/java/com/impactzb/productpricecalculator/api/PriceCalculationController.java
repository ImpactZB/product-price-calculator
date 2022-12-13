package com.impactzb.productpricecalculator.api;

import com.impactzb.productpricecalculator.data.dto.PriceDto;
import com.impactzb.productpricecalculator.data.model.DiscountType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.impactzb.productpricecalculator.service.price.PriceService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = PriceCalculationController.PRICE_ENDPOINT)
public class PriceCalculationController {

    public static final String PRICE_ENDPOINT = "/product/{productId}/price";

    private final PriceService priceService;

    @GetMapping(produces = "application/json")
    @Operation(description="Return price for given product, its amount and selected discount")
    public ResponseEntity<?> getProductPrice(@PathVariable(value = "productId") UUID productId, @RequestParam(value = "amount") Long amount,
            @RequestParam(value = "discountType", required = false) DiscountType discountType){
        PriceDto priceDto = priceService.getProductPrice(productId, amount, discountType);
        return new ResponseEntity<>(priceDto, new HttpHeaders(), HttpStatus.OK);
    }
}