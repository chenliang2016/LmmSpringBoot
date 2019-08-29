package com.lmm.zuul;

import com.lmm.zuul.filters.ErrorFilter;
import com.lmm.zuul.filters.PostFilter;
import com.lmm.zuul.filters.SignFilter;
import com.lmm.zuul.filters.RouteFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class LmmZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmmZuulApplication.class, args);
    }

    @Bean
    public SignFilter preFilter() {
        return new SignFilter();
    }
    @Bean
    public PostFilter postFilter() {
        return new PostFilter();
    }
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }
    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }
}
