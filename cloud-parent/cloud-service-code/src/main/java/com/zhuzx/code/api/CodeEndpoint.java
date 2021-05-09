package com.zhuzx.code.api;

import com.zhuzx.api.EmailApi;
import com.zhuzx.code.pojo.AuthCode;
import com.zhuzx.code.repository.AuthCodeRepository;


import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName CodeEndpoint
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 13:47
 * @Version 1.0
 **/
@RestController
@RequestMapping("/code")
public class CodeEndpoint {

    private AuthCodeRepository authCodeRepository;

    @Reference
    private EmailApi emailApi;

    @GetMapping("/create/{email}")
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(@PathVariable("email") String email) {
        String code = String.format("%d", getCode());

        AuthCode authCode = new AuthCode();
        authCode.setCode(code);
        authCode.setEmail(email);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(10);
        authCode.setCreateTime(now);
        authCode.setExpireTime(expireTime);
        authCodeRepository.save(authCode);
        return emailApi.sendEmail(email, code);
    }

    private int getCode() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }


    @Autowired
    public void setAuthCodeRepository(AuthCodeRepository authCodeRepository) {
        this.authCodeRepository = authCodeRepository;
    }
}
