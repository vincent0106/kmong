package com.simple.mall.order;

import com.simple.mall.ResultMessage;
import com.simple.mall.Utils;
import com.simple.mall.entity.Order;
import com.simple.mall.entity.OrderProduct;
import com.simple.mall.entity.Product;
import com.simple.mall.repository.OrderProductRepository;
import com.simple.mall.repository.OrderRepository;
import com.simple.mall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Transactional
    public Map<String, Object> order(Map<String, Object> data, HttpSession session){

        if ( !Utils.isSigned(session) ){
            return ResultMessage.COMMON_00001.toMap();
        }

        Long memberNo = (Long) session.getAttribute("memberNo");

        if ( data.get("products") == null || !(data.get("products") instanceof List)){
            return ResultMessage.ORDER_10001.toMap();
        }

        long totalPrice = 0L;
        List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");

        List<OrderProduct> insertPorudcts = new ArrayList<>();

        for (Map<String, Object> product : products) {

            Long productId = Long.valueOf(product.get("productId").toString());

            Optional<Product> selectProduct = productRepository.findById(productId);

            if ( selectProduct.isEmpty() ){
                return ResultMessage.ORDER_10002.toMap();
            }

            int quantity = Integer.parseInt(product.get("quantity").toString());
            Long price = selectProduct.get().getPrice();

            totalPrice += quantity * price;

            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .salesPrice(selectProduct.get().getPrice())
                    .build();
            insertPorudcts.add(orderProduct);
        }

        Order order = Order.builder().memberNo(memberNo).totalPrice(totalPrice).build();

        Order orderResult = orderRepository.save(order);
        Long orderNo = orderResult.getOrderNo();

        for (OrderProduct insertProduct : insertPorudcts) {
            insertProduct.setOrderNo(orderNo);
            orderProductRepository.save(insertProduct);
        }

        Map<String, Object> result = ResultMessage.ORDER_00001.toMap();
        result.put("orderNo", orderNo);
        return result;
    }

    public Map<String, Object> read(String orderNo, HttpSession session){

        Optional<Order> order = orderRepository.findById(Long.valueOf(orderNo));

        Long memberNo = (Long) session.getAttribute("memberNo");

        if ( order.isEmpty() || !Objects.equals(order.get().getMemberNo(), memberNo)){
            return ResultMessage.ORDER_10003.toMap();
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderNo(Long.valueOf(orderNo));

        Map<String, Object> result = ResultMessage.ORDER_00002.toMap();
        result.put("orderNo", order.get().getOrderNo());
        result.put("totalPrice", order.get().getTotalPrice());
        result.put("products", orderProducts);
        return result;
    }
}
