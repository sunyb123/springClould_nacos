package com.zhuzx.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @ClassName EmailApp
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 13:56
 * @Version 1.0
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class EmailApp {

    public static void main(String[] args) {
        SpringApplication.run(EmailApp.class, args);
    }
}
