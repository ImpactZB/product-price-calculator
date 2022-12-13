package com.impactzb.productpricecalculator.api;

import com.impactzb.productpricecalculator.data.dto.AmountBasedDiscountDto;
import com.impactzb.productpricecalculator.data.dto.DiscountDto;
import com.impactzb.productpricecalculator.data.dto.PercentageBasedDiscountDto;
import com.impactzb.productpricecalculator.service.discount.AmountBasedDiscountService;
import com.impactzb.productpricecalculator.service.discount.DiscountService;
import com.impactzb.productpricecalculator.service.discount.PercentageBasedDiscountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = DiscountController.DISCOUNT_ENDPOINT)
public class DiscountController {

    public static final String DISCOUNT_ENDPOINT = "/product/{productId}/discount";

    private final DiscountService discountService;

    private final PercentageBasedDiscountService percentageBasedDiscountService;

    private final AmountBasedDiscountService amountBasedDiscountService;

    @GetMapping(produces = "application/json")
    @Operation(description="Return list of all product discounts")
    public ResponseEntity<?> getDiscountsForProduct(@PathVariable("productId") UUID productId){
        Set<DiscountDto> discountDtoSet = discountService.getProductDiscountDtoSet(productId);
        return new ResponseEntity<>(discountDtoSet, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(path= "/percentageBasedDiscount")
    @Operation(description="Create new percentage based discount")
    public ResponseEntity<?> createDiscount(@PathVariable("productId") UUID productId, @RequestBody @Valid PercentageBasedDiscountDto discountDto){
        PercentageBasedDiscountDto percentageBasedDiscountDto = percentageBasedDiscountService.createPercentageBasedDiscount(productId, discountDto);
        return new ResponseEntity<>(percentageBasedDiscountDto, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PostMapping(path= "/amountBasedDiscount")
    @Operation(description="Create new amount based discount")
    public ResponseEntity<?> createDiscount(@PathVariable("productId") UUID productId, @RequestBody @Valid AmountBasedDiscountDto discountDto){
        AmountBasedDiscountDto amountBasedDiscountDto = amountBasedDiscountService.createAmountBasedDiscount(productId, discountDto);
        return new ResponseEntity<>(amountBasedDiscountDto, new HttpHeaders(), HttpStatus.CREATED);
    }

    @DeleteMapping(path= "/{discountId}")
    @Operation(description="Remove discount")
    public ResponseEntity<?> deleteDiscount(@PathVariable("discountId") UUID id){
        discountService.deleteDiscount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}