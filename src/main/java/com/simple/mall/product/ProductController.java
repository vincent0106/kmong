package com.simple.mall.product;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prd")
@Api(tags="상품")
public class ProductController {

    @Autowired
    ProductService productService;

    /*
    * 상품 조회
    */
    @GetMapping("/{productNo}")
    @ApiOperation(value="상품조회")
    @ApiImplicitParam(name = "productNo", value = "상품번호")
    public ResponseEntity<?> read(@PathVariable String productNo){
        return new ResponseEntity<>(productService.read(productNo), HttpStatus.OK);
    }
}
