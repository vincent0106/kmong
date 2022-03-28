package com.simple.mall.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "회원정보")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(length = 100, nullable = false, unique = true)
    @ApiParam(value = "아이디", required = true)
    @ApiModelProperty(example = "tester")
    private String id;

    @Column(length = 200, nullable = false, unique = true)
    @ApiParam(value = "이메일", required = true)
    @ApiModelProperty(example = "tester@gmail.com")
    private String email;

    @Column(length = 200, nullable = false)
    @ApiParam(value = "비밀번호", required = true)
    @ApiModelProperty(example = "tester123!@#$")
    private String password;

    @Column(length = 200, nullable = false)
    @ApiParam(value = "이름", required = true)
    @ApiModelProperty(example = "테스터")
    private String name;


}
