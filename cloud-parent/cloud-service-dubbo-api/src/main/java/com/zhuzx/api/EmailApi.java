package com.zhuzx.api;

/**
 * @ClassName EmailApi
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 11:28
 * @Version 1.0
 **/
public interface EmailApi {
    Boolean sendEmail(String email, String code);
}
