package com.zhuzx.gateway.filter;

import com.google.common.collect.Lists;
import com.zhuzx.api.UserApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpCookie;
import org.springframework.util.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @ClassName UserTokenFilter
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 11:49
 * @Version 1.0
 **/
@Slf4j
@Component
public class UserTokenFilter implements GlobalFilter {

    private UserApi userApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final List<String> passList = Lists.newArrayList("/user", "/code");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        boolean noNeedValidate = passList.stream().anyMatch(path::startsWith);
        if (noNeedValidate) {
            return chain.filter(exchange);
        }

        List<HttpCookie> token = request.getCookies().get("token");
        if (CollectionUtils.isEmpty(token)) {
            return unauthorized(exchange);
        }
        String value = token.get(0).getValue();
        String username = userApi.userInfo(value);
        if (!StringUtils.hasText(username)) {
            return unauthorized(exchange);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap("UNAUTHORIZED".getBytes());
        response.writeWith(Mono.just(buffer));
        return Mono.empty();
    }

    @Reference
    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
    }
}
