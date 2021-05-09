package com.zhuzx.mail.api;

import com.zhuzx.api.EmailApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @ClassName EmailApiImpl
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 13:57
 * @Version 1.0
 **/
@Slf4j
@Service(timeout = 30000)
public class EmailApiImpl implements EmailApi {

    private JavaMailSender javaMailSender;

    @Override
    public Boolean sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1255624301@qq.com");
        message.setTo(email);
        message.setSubject("注册验证码");
        message.setText(String.format("验证码为：%s", code));
        try {
            javaMailSender.send(message);
        } catch (Exception ex) {
            log.error("send email err. email: {}, ", email, ex);
            return false;
        }
        return true;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
