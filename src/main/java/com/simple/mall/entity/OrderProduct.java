package com.simple.mall.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "orderProduct")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductNo;

    @Column(nullable = false)
    private Long orderNo;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long salesPrice;

    @Column(nullable = false)
    private Integer quantity;

}
