package com.zhuzx.code.api;

import com.zhuzx.api.CodeApi;
import com.zhuzx.code.pojo.AuthCode;
import com.zhuzx.code.repository.AuthCodeRepository;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @ClassName SoaCodeApi
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 13:49
 * @Version 1.0
 **/
@Service(timeout = 30000)
@org.springframework.stereotype.Service
public class SoaCodeApi implements CodeApi {

    private AuthCodeRepository authCodeRepository;

    /**
     * @return 校验验证码是否正确，0正确1错误2超时
     */
    @Override
    public Integer validate(String email, String code) {
        AuthCode authCode = authCodeRepository.findByEmailOrderByCreateTimeDesc(email).get(0);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(authCode.getExpireTime())) {
            return 2;
        }
        if (!authCode.getCode().equals(code)) {
            return 1;
        }
        return 0;
    }

    @Autowired
    public void setAuthCodeRepository(AuthCodeRepository authCodeRepository) {
        this.authCodeRepository = authCodeRepository;
    }
}
