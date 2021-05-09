package com.zhuzx.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * @ClassName AuthCode
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 11:35
 * @Version 1.0
 **/
@Data
@Entity
@Table(name = "lagou_auth_code")
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column
    private String code;

    @Column(name = "createtime")
    private LocalDateTime createTime;

    @Column(name = "expiretime")
    private LocalDateTime expireTime;
}
