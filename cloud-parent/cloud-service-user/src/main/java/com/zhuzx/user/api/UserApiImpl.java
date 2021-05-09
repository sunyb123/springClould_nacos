package com.zhuzx.user.api;

import com.zhuzx.api.CodeApi;
import com.zhuzx.api.UserApi;
import com.zhuzx.user.pojo.Token;
import com.zhuzx.user.pojo.User;
import com.zhuzx.user.repository.TokenRepository;
import com.zhuzx.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @ClassName UserApiImpl
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 14:00
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/user")
@Service(timeout = 30000)
public class UserApiImpl implements UserApi {

    private TokenRepository tokenRepository;

    private UserRepository userRepository;

    @Reference
    private CodeApi codeApi;

    @GetMapping("/register/{email}/{password}/{code}")
    public Boolean register(@PathVariable("email") String email,
                            @PathVariable("password") String password,
                            @PathVariable("code") String code,
                            HttpServletResponse response) {
        Integer validateResult = codeApi.validate(email, code);
        if (validateResult != 0) {
            return false;
        }
        if (isRegister(email)) {
            return false;
        }
        User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }

    @GetMapping("/isRegistered/{email}")
    public Boolean isRegister(@PathVariable("email") String email) {
        User user = userRepository.findByUsername(email);
        return null != user;
    }

    @GetMapping("/login/{email}/{password}")
    public String login(@PathVariable("email") String email,
                        @PathVariable("password") String password,
                        HttpServletResponse response) {
        User user = userRepository.findByUsernameAndPassword(email, password);
        if (null == user) {
            return null;
        }
        generateAndSetToken(response, user);
        return email;
    }

    private void generateAndSetToken(HttpServletResponse response, User user) {
        String token = UUID.randomUUID().toString();
        saveToken(user, token);

        Cookie cookie = new Cookie("token", token);
        cookie.setDomain("www.test.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void saveToken(User user, String token) {
        Token byEmail = tokenRepository.findByEmail(user.getUsername());
        if (null != byEmail) {
            byEmail.setToken(token);
            tokenRepository.save(byEmail);
        } else {
            Token repoToken = new Token();
            repoToken.setEmail(user.getUsername());
            repoToken.setToken(token);
            tokenRepository.save(repoToken);
        }
    }

    @GetMapping("/info/{token}")
    @Override
    public String userInfo(@PathVariable("token") String token) {
        Token byToken = tokenRepository.findByToken(token);
        if (null == byToken) {
            return null;
        }
        return byToken.getEmail();
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
