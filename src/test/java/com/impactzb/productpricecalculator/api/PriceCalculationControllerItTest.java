package com.impactzb.productpricecalculator.api;

import com.impactzb.productpricecalculator.ProductPriceCalculatorApplication;
import com.impactzb.productpricecalculator.data.model.AmountBasedDiscount;
import com.impactzb.productpricecalculator.data.model.DiscountType;
import com.impactzb.productpricecalculator.data.model.PercentageBasedDiscount;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.DiscountRepository;
import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import com.impactzb.productpricecalculator.util.AppStartupRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ProductPriceCalculatorApplication.class, AppStartupRunner.class})
@AutoConfigureMockMvc
class PriceCalculationControllerItTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Test
    @DisplayName("API GET /product/{productId/price - calculate valid price without discount")
    @Transactional
    void testValidPriceCalculationWithoutDiscount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(150.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        UUID productId = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().map(Product::getId).get();
        long productAmount = 10L;

        //when
        mvc.perform(get(String.format("/product/%s/price", productId))
                        .param("amount", Long.toString(productAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(BigDecimal.valueOf(1500).setScale(1, RoundingMode.HALF_UP).doubleValue())));
    }

    @Test
    @DisplayName("API GET /product/{productId/price - calculate valid price with percentage discount")
    @Transactional
    void testValidPriceCalculationWithPercentageDiscount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(1000.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();

        PercentageBasedDiscount percentageBasedDiscount = PercentageBasedDiscount.builder().product(product).percentage(BigDecimal.TEN).build();
        discountRepository.save(percentageBasedDiscount);

        long productAmount = 10L;

        //when
        mvc.perform(get(String.format("/product/%s/price", productId))
                        .param("amount", Long.toString(productAmount))
                        .param("discountType", DiscountType.PERCENTAGE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(BigDecimal.valueOf(9000).setScale(1, RoundingMode.HALF_UP).doubleValue())));
    }

    @Test
    @DisplayName("API GET /product/{productId/price - calculate valid price amount based discount")
    @Transactional
    void testValidPriceCalculationWithAmountBasedDiscount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(10.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();

        AmountBasedDiscount amountBasedDiscount1 = AmountBasedDiscount.builder().product(product).percentage(BigDecimal.valueOf(5)).amount(5L).build();
        AmountBasedDiscount amountBasedDiscount2 = AmountBasedDiscount.builder().product(product).percentage(BigDecimal.TEN).amount(10L).build();
        AmountBasedDiscount amountBasedDiscount3 = AmountBasedDiscount.builder().product(product).percentage(BigDecimal.valueOf(20)).amount(50L).build();
        discountRepository.saveAll(Set.of(amountBasedDiscount1, amountBasedDiscount2, amountBasedDiscount3));

        long productAmount = 100L;

        //when
        mvc.perform(get(String.format("/product/%s/price", productId))
                        .param("amount", Long.toString(productAmount))
                        .param("discountType", DiscountType.AMOUNT.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(BigDecimal.valueOf(800).setScale(1, RoundingMode.HALF_UP).doubleValue())));
    }
}
