package com.simple.mall.member;

import com.simple.mall.ResultMessage;
import com.simple.mall.Utils;
import com.simple.mall.entity.Member;
import com.simple.mall.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    /* 로그인 */
    public Map<String, Object> login(Member member, HttpSession session){
        Map<String, Object> result = new HashMap<>();

        // 1. ID, 이메일, 비밀번호 필수값 체크
        if(member.getId().isBlank() || member.getPassword().isBlank()){
            return ResultMessage.MEMBER_10003.toMap();
        }

        Member selectMember = memberRepository.findMemberById(member.getId());

        if ( selectMember == null ){
            return ResultMessage.MEMBER_10004.toMap();
        }
        // 비밀번호 확인
        if ( !BCrypt.checkpw(member.getPassword(), selectMember.getPassword()) ){
            return ResultMessage.MEMBER_10004.toMap();
        }

        // 2. 회원 세션 생성
        session.setAttribute("memberNo", selectMember.getMemberNo());
        session.setAttribute("name", selectMember.getName());
        return ResultMessage.MEMBER_00003.toMap();
    }

    /* 회원가입 */
    public Map<String, Object> signUp(Member member){

        // 1. ID, 이메일, 비밀번호 필수값 체크
        if(member.getId().isBlank()
                || member.getName().isBlank()
                || member.getEmail().isBlank()
                || member.getPassword().isBlank()){
            return ResultMessage.MEMBER_10003.toMap();
        }
        // 2. 회원가입여부 체크
        if ( memberRepository.countById(member.getId()) > 0 ){
            return ResultMessage.MEMBER_10001.toMap();
        }

        if ( memberRepository.countByEmail(member.getEmail()) > 0 ){
            return ResultMessage.MEMBER_10002.toMap();
        }

        member.setPassword(encrypt(member.getPassword()));

        // 3. 회원가입
        memberRepository.save(member);
        return ResultMessage.MEMBER_00001.toMap();
    }

    public Map<String, Object> logout(HttpSession session){
        session.invalidate();
        return ResultMessage.MEMBER_00002.toMap();
    }

    public Map<String, Object> me(HttpSession session){
        Map<String, Object> result = new HashMap<>();
        result.put("signed", Utils.isSigned(session));
        result.put("code", 200);
        result.put("message", "성공");
        return result;
    }

    public String encrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
