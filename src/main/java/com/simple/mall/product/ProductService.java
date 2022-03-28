package com.simple.mall.product;

import com.simple.mall.ResultMessage;
import com.simple.mall.entity.Product;
import com.simple.mall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Map<String, Object> read(String productNo){
        Map<String, Object> result = new HashMap<>();

        if (productNo.isBlank()){
            result = ResultMessage.PRODUCT_10001.toMap();
            result.put("product", null);
            return result;
        }

        Optional<Product> product = productRepository.findById(Long.valueOf(productNo));

        if ( product.isEmpty() ){
            result = ResultMessage.PRODUCT_10001.toMap();
            result.put("product", null);
            return result;
        }

        result = ResultMessage.PRODUCT_00001.toMap();
        result.put("product", product);
        return result;
    }
}
