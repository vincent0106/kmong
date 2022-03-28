package com.simple.mall.product;

import com.simple.mall.entity.Product;
import com.simple.mall.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    Long productId;

    @BeforeEach
    void setUp() {
        Product product = productRepository.save(Product.builder().name("테스트 상품1").price(10000L).build());
        productId = product.getProductId();
    }

    /*
     * 조회 성공
     */
    @Test
    void read() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/prd/" + productId));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCT_00001"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.product.productId").value(productId))
                .andExpect(jsonPath("$.product.name").value("테스트 상품1"))
                .andExpect(jsonPath("$.product.price").value(10000));
    }

    /*
     * 실패 : 상품 아이디 없음.
     */
    @Test
    void read_notFound() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/prd/991001"));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCT_10001"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.product").isEmpty());
    }
}
