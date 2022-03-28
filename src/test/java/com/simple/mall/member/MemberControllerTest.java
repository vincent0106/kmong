package com.simple.mall.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.mall.entity.Member;
import com.simple.mall.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.builder()
                .id("junitTester1")
                .email("junitTester1@gmail.com")
                .name("테스터1")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt())).build());
    }

    /*
     * 로그인 성공
     * */
    @Test
    void login_success() throws Exception {
        String param = new ObjectMapper().writeValueAsString(Member.builder().id("junitTester1").password("1234").build());
        //given
        ResultActions resultActions = mockMvc.perform(post("/mem/login")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_00003"))
                .andExpect(jsonPath("$.message").exists());
    }
    /*
    * 실패 : 패스워드 일치 에러
    * */
    @Test
    void login_fail_passwordError() throws Exception {
        String param = new ObjectMapper().writeValueAsString(Member.builder().id("junitTester1").password("1234567890").build());
        //given
        ResultActions resultActions = mockMvc.perform(post("/mem/login")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_10004"))
                .andExpect(jsonPath("$.message").value("아이디 혹은 비밀번호를 확인해주세요."));
    }
    /*
     * 회원가입 성공
     * */
    @Test
    void signUp_success() throws Exception {
        String param = new ObjectMapper().writeValueAsString(Member.builder()
                .id("junitTester2")
                .name("테스터2")
                .email("junitTester2@gmail.com")
                .password("1234567890").build());
        //given
        ResultActions resultActions = mockMvc.perform(post("/mem/signUp")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_00001"))
                .andExpect(jsonPath("$.message").value("회원가입되었습니다."));
    }

    /*
     * 회원가입 실패 : 이메일 중복
     * */
    @Test
    void signUp_fail_email() throws Exception {
        String param = new ObjectMapper().writeValueAsString(Member.builder()
                .id("junitTester2")
                .name("테스터2")
                .email("junitTester1@gmail.com")
                .password("1234567890").build());
        //given
        ResultActions resultActions = mockMvc.perform(post("/mem/signUp")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_10002"))
                .andExpect(jsonPath("$.message").value("이미 사용중인 이메일입니다."));
    }

    /*
     * 회원가입 실패 : 필수값 체크 - name
     * */
    @Test
    void signUp_fail_name() throws Exception {
        String param = new ObjectMapper().writeValueAsString(Member.builder()
                .id("junitTester2")
                .name("테스터2")
                .email("")
                .password("1234567890").build());
        //given
        ResultActions resultActions = mockMvc.perform(post("/mem/signUp")
                .content(param)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_10003"))
                .andExpect(jsonPath("$.message").value("필수값을 확인해주세요."));
    }

    /*
     * 로그아웃 성공
     * */
    @Test
    void logout() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/mem/logout"));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_00002"))
                .andExpect(jsonPath("$.message").value("로그아웃되었습니다."));
    }

    @Test
    void me() {
    }
}
