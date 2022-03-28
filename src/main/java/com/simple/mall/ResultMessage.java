package com.simple.mall;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ResultMessage {

    COMMON_00001("로그인이 필요합니다."),

    // 회원 SUCCESS
    MEMBER_00001("회원가입되었습니다."),
    MEMBER_00002("로그아웃되었습니다."),
    MEMBER_00003("로그인되었습니다."),

    // 회원 ERROR
    MEMBER_10001("이미 사용중인 아이디입니다."),
    MEMBER_10002("이미 사용중인 이메일입니다."),
    MEMBER_10003("필수값을 확인해주세요."),
    MEMBER_10004("아이디 혹은 비밀번호를 확인해주세요."),


    // 상품 SUCCESS
    PRODUCT_00001("성공"),

    // 상품 ERROR
    PRODUCT_10001("상품을 찾을 수 없습니다."),


    // 주문
    ORDER_00001("주문완료 되었습니다."),
    ORDER_00002("주문조회 완료"),

    ORDER_10001("주문상품 내용이 없습니다."),
    ORDER_10002("상품을 찾을 수 없습니다."),
    ORDER_10003("주문을 찾을 수 없습니디.");

    String message;

    ResultMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("code", this.toString());
        result.put("message", this.getMessage());
        return result;
    }

}
