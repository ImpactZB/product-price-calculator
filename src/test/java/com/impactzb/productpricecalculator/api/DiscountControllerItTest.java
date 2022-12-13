package com.impactzb.productpricecalculator.api;

import com.impactzb.productpricecalculator.ProductPriceCalculatorApplication;
import com.impactzb.productpricecalculator.data.model.AmountBasedDiscount;
import com.impactzb.productpricecalculator.data.model.PercentageBasedDiscount;
import com.impactzb.productpricecalculator.data.model.Product;
import com.impactzb.productpricecalculator.data.repository.AmountBasedDiscountRepository;
import com.impactzb.productpricecalculator.data.repository.PercentageBasedDiscountRepository;
import com.impactzb.productpricecalculator.data.repository.ProductRepository;
import com.impactzb.productpricecalculator.exception.handler.GlobalExceptionHandler;
import com.impactzb.productpricecalculator.util.AppStartupRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ProductPriceCalculatorApplication.class, AppStartupRunner.class})
@AutoConfigureMockMvc
class DiscountControllerItTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PercentageBasedDiscountRepository percentageBasedDiscountRepository;

    @Autowired
    AmountBasedDiscountRepository amountBasedDiscountRepository;

    @Test
    @DisplayName("API POST /product/{productId}/discount/percentageBasedDiscount - should successfully create percentage based discount")
    @Transactional
    void testPercentageBasedDiscountCreated() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        UUID productId = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().map(Product::getId).get();
        BigDecimal discountPercentage = BigDecimal.TEN.setScale(1, RoundingMode.HALF_UP);

        //when
        mvc.perform(post(String.format("/product/%s/discount/percentageBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.discountType", is(PercentageBasedDiscount.DISCRIMINATOR)))
                .andExpect(jsonPath("$.percentage", is(discountPercentage.setScale(1, RoundingMode.HALF_UP).doubleValue())));

        //then
        assertTrue(percentageBasedDiscountRepository.findByPercentageAndProductId(discountPercentage, productId).isPresent());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/percentageBasedDiscount - should throw error for creating duplicated percentage based discount")
    @Transactional
    void testErrorThrownForCreatingDuplicatedPercentageBasedDiscount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.TEN.setScale(1, RoundingMode.HALF_UP);

        PercentageBasedDiscount percentageBasedDiscount = PercentageBasedDiscount.builder().percentage(discountPercentage).product(product).build();
        percentageBasedDiscountRepository.save(percentageBasedDiscount);

        //when
        mvc.perform(post(String.format("/product/%s/discount/percentageBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Exactly same product discount already exists")));

        //then
        assertEquals(1, percentageBasedDiscountRepository.findAll().stream()
                .filter(discount -> productId.equals(discount.getProduct().getId())
                        && discountPercentage.equals(discount.getPercentage()))
                .count());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/percentageBasedDiscount - should throw error for creating percentage based discount with invalid percentage")
    @Transactional
    void testErrorThrownForCreatingPercentageBasedDiscountWithInvalidPercentage() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.valueOf(-1);

        //when
        mvc.perform(post(String.format("/product/%s/discount/percentageBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Percentage value has to be equal or greater than 0 and lower or equal to 100")));

        //then
        assertTrue(percentageBasedDiscountRepository.findByPercentageAndProductId(discountPercentage, productId).isEmpty());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/percentageBasedDiscount - should throw error for creating percentage based discount without percentage value provided")
    @Transactional
    void testErrorThrownForCreatingPercentageBasedDiscountWithoutPercentageValueProvided() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();

        //when
        mvc.perform(post(String.format("/product/%s/discount/percentageBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Percentage value is mandatory")));
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should successfully create amount based discount")
    @Transactional
    void testAmountBasedDiscountCreated() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(115.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        UUID productId = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().map(Product::getId).get();
        BigDecimal discountPercentage = BigDecimal.TEN.setScale(1, RoundingMode.HALF_UP);
        long amount = 5L;

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\", \"amount\": \"" + amount + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.discountType", is(AmountBasedDiscount.DISCRIMINATOR)))
                .andExpect(jsonPath("$.percentage", is(discountPercentage.setScale(1, RoundingMode.HALF_UP).doubleValue())));

        //then
        assertTrue(amountBasedDiscountRepository.findByPercentageAndAmountAndProductId(discountPercentage, amount, productId).isPresent());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should throw error for creating duplicated amount based discount")
    @Transactional
    void testErrorThrownForCreatingDuplicatedAmountBasedDiscount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.TEN.setScale(1, RoundingMode.HALF_UP);
        Long amount = 5L;

        AmountBasedDiscount amountBasedDiscount = AmountBasedDiscount.builder().percentage(discountPercentage).amount(amount).product(product).build();
        amountBasedDiscountRepository.save(amountBasedDiscount);

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\", \"amount\": \"" + amount + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Exactly same product discount already exists")));

        //then
        assertEquals(1, amountBasedDiscountRepository.findAll().stream()
                .filter(discount -> productId.equals(discount.getProduct().getId())
                        && discountPercentage.equals(discount.getPercentage())
                        && amount.equals(discount.getAmount()))
                .count());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should throw error for creating amount based discount with invalid percentage")
    @Transactional
    void testErrorThrownForCreatingAmountBasedDiscountWithInvalidPercentage() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.valueOf(101);
        long amount = 5L;

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\", \"amount\": \"" + amount + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Percentage value has to be equal or greater than 0 and lower or equal to 100")));

        //then
        assertTrue(percentageBasedDiscountRepository.findByPercentageAndProductId(discountPercentage, productId).isEmpty());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should throw error for creating amount based discount with invalid amount")
    @Transactional
    void testErrorThrownForCreatingAmountBasedDiscountWithInvalidAmount() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.valueOf(20);
        long amount = 0L;

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\", \"amount\": \"" + amount + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Amount value has to be equal or greater than 1")));

        //then
        assertTrue(percentageBasedDiscountRepository.findByPercentageAndProductId(discountPercentage, productId).isEmpty());
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should throw error for creating amount based discount without amount value provided")
    @Transactional
    void testErrorThrownForCreatingAmountBasedDiscountWithoutAmountValueProvided() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();
        BigDecimal discountPercentage = BigDecimal.valueOf(20);

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"percentage\": \"" + discountPercentage + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Amount value is mandatory")));
    }

    @Test
    @DisplayName("API POST /product/{productId}/discount/amountBasedDiscount - should throw error for creating amount based discount without percentage value provided")
    @Transactional
    void testErrorThrownForCreatingAmountBasedDiscountWithoutPercentageValueProvided() throws Exception {
        //given
        BigDecimal productPrice = BigDecimal.valueOf(111.00);
        Product product = Product.builder().price(productPrice).build();
        productRepository.save(product);
        product = productRepository.findAll().stream().filter(product1 -> productPrice.equals(product1.getPrice())).findAny().get();
        UUID productId = product.getId();;
        long amount = 10L;

        //when
        mvc.perform(post(String.format("/product/%s/discount/amountBasedDiscount", productId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": \"" + amount + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.title", is(GlobalExceptionHandler.BAD_REQUEST_TITLE)))
                .andExpect(jsonPath("$.details[0]", is("Percentage value is mandatory")));
    }
}
