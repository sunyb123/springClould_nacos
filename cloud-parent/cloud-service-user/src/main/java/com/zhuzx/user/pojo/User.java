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
@Table(name = "lagou_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;
}
