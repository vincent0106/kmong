package com.simple.mall.member;

import com.simple.mall.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/mem")
@Api(tags="회원")
public class MemberController {

    @Autowired
    MemberService memberService;

    /*
    * 로그인
    * */
    @PostMapping("/login")
    @ApiOperation(value="로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "아이디"),
            @ApiImplicitParam(name = "password", value = "비밀번호")
    })
    public ResponseEntity<?> login(@RequestBody Member member, HttpSession session){
        return new ResponseEntity<>(memberService.login(member, session), HttpStatus.OK);
    }

    /*
    * 회원가입
    * */
    @PostMapping("/signUp")
    @ApiOperation(value="회원가입")
    public ResponseEntity<?> signUp(@RequestBody Member member){
        return new ResponseEntity<>(memberService.signUp(member), HttpStatus.OK);
    }

    /*
    * 로그아웃
    * */
    @GetMapping("/logout")
    @ApiOperation(value="로그아웃")
    public ResponseEntity<?> logout(HttpSession session){
        return new ResponseEntity<>(memberService.logout(session), HttpStatus.OK);
    }

}
