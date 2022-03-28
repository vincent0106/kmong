package com.simple.mall.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Api(tags="주문")
public class OrderController {

    @Autowired
    OrderService orderService;
    /*
    * 상품 주문
    * */
    @PostMapping("/order")
    @ApiOperation(value="주문")
    public ResponseEntity<?> order(@RequestBody Map<String, Object> data, HttpSession session){
        return new ResponseEntity<>(orderService.order(data, session), HttpStatus.OK);
    }

    /*
     * 회원 주문 내역 조회
     * */
    @GetMapping("/{orderNo}")
    @ApiOperation(value="주문조회")
    @ApiImplicitParam(name = "orderNo", value = "주문번호")
    public ResponseEntity<?> readOrder(@PathVariable String orderNo, HttpSession session){
        return new ResponseEntity<>(orderService.read(orderNo, session), HttpStatus.OK);
    }
}
