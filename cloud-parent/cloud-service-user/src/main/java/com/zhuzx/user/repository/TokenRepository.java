package com.zhuzx.user.repository;

import com.zhuzx.user.pojo.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName TokenRepository
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 14:01
 * @Version 1.0
 **/
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByToken(String token);

    Token findByEmail(String email);
}
