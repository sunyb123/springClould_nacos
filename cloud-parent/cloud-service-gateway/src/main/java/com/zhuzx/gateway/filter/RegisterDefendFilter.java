package com.zhuzx.gateway.filter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * @ClassName RegisterDefendFilter
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 11:41
 * @Version 1.0
 **/
@Slf4j
@Component
@RefreshScope
public class RegisterDefendFilter implements GlobalFilter, Ordered {

    @Value("${gateway.x}")
    private Integer x;

    @Value("${gateway.y}")
    private Integer y;

    private ConcurrentHashMap<String, List<LocalDateTime>> metrics = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String registerPathPrefix = "/api/user/register";
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String hostName = request.getRemoteAddress().getHostString();
        log.info("register defend filter, x:{}, y:{}, path: {}, hostName: {}", x, y, path, hostName);
        if (!path.startsWith(registerPathPrefix)) {
            return chain.filter(exchange);
        }

        List<LocalDateTime> newList = Optional.ofNullable(metrics.putIfAbsent(hostName, Lists.newArrayList()))
                .orElse(Lists.newArrayList())
                .stream().filter(ele -> {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime before = now.minusMinutes(x);
                    return ele.isAfter(before) && ele.isBefore(now);
                }).collect(Collectors.toList());
        metrics.put(hostName, newList);
        if (newList.size() >= y) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            DataBuffer buffer = response.bufferFactory().wrap("denied".getBytes());
            response.writeWith(Mono.just(buffer));
            return Mono.empty();
        }
        newList.add(LocalDateTime.now());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
