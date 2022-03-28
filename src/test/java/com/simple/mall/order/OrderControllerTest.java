package com.simple.mall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.mall.entity.Product;
import com.simple.mall.repository.MemberRepository;
import com.simple.mall.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    protected MockHttpSession session;

    Long productId;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("memberNo", 1L);
        session.setAttribute("name", "테스터");
        session.setAttribute("email", "test@naver.com");

        Product product = productRepository.save(Product.builder().name("테스트 상품1").price(10000L).build());
        productId = product.getProductId();
    }
    /*
    * 주문 성공
    * */
    @Test
    void order() throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("productId", productId);
        product.put("quantity", 2);
        products.add(product);

        paramMap.put("products", products);

        String param = new ObjectMapper().writeValueAsString(paramMap);
        //given
        ResultActions resultActions = mockMvc.perform(post("/order/order")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORDER_00001"))
                .andExpect(jsonPath("$.message").value("주문완료 되었습니다."))
                .andExpect(jsonPath("$.orderNo").exists());
    }

    /*
     * 주문 실패 - 상품번호 불일치
     * */
    @Test
    void order_error_notFoundProduct() throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("productId", 999999001);
        product.put("quantity", 2);
        products.add(product);

        paramMap.put("products", products);

        String param = new ObjectMapper().writeValueAsString(paramMap);
        //given
        ResultActions resultActions = mockMvc.perform(post("/order/order")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORDER_10002"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다."));
    }


    Long beforeOrder() throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("productId", productId);
        product.put("quantity", 2);
        products.add(product);

        paramMap.put("products", products);

        String param = new ObjectMapper().writeValueAsString(paramMap);
        //given
        ResultActions resultActions = mockMvc.perform(post("/order/order")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session));
        //then
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORDER_00001"))
                .andExpect(jsonPath("$.message").value("주문완료 되었습니다."))
                .andExpect(jsonPath("$.orderNo").exists()).andReturn();

        Map result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        return Long.valueOf(result.get("orderNo").toString());
    }

    /*
     * 주문 조회 성공
     * */
    @Test
    void readOrder() throws Exception {

        Long orderNo = beforeOrder();

        //given
        ResultActions resultActions = mockMvc.perform(get("/order/" + orderNo).session(session));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value(orderNo))
                .andExpect(jsonPath("$.totalPrice").value(20000))
                .andExpect(jsonPath("$.products[0].productId").value(1001))
                .andExpect(jsonPath("$.products[0].salesPrice").value(10000))
                .andExpect(jsonPath("$.products[0].quantity").value(2));
    }

    /*
    * 주문 조회 실패 - 해당 주문번호 없음
    * */
    @Test
    void readOrder_error_notFound() throws Exception {

        Long orderNo = beforeOrder();

        //given
        ResultActions resultActions = mockMvc.perform(get("/order/" + orderNo).session(session));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value(orderNo))
                .andExpect(jsonPath("$.totalPrice").value(20000))
                .andExpect(jsonPath("$.products[0].productId").value(1001))
                .andExpect(jsonPath("$.products[0].salesPrice").value(10000))
                .andExpect(jsonPath("$.products[0].quantity").value(2));
    }
}
