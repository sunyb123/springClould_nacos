package com.zhuzx.code.repository;


import com.zhuzx.code.pojo.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName AuthCodeRepository
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 13:49
 * @Version 1.0
 **/
@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Integer> {
    /**
     * 根据邮箱查询登录信息
     * @param email
     * @return
     */
    List<AuthCode> findByEmailOrderByCreateTimeDesc(String email);
}
