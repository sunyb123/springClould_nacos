package com.zhuzx.user.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName A
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/3/30 15:12
 * @Version 1.0
 **/
@Data
@Entity
@Table(name = "lagou_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 邮箱地址
     */
    @Column
    private String email;

    /**
     * 令牌
     */
    @Column
    private String token;

}