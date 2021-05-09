package com.zhuzx.api;

/**
 * @ClassName CodeApi
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 11:28
 * @Version 1.0
 **/
public interface CodeApi {
    /**
     * @return 校验验证码是否正确，0正确1错误2超时
     */
    Integer validate(String email, String code);
}
