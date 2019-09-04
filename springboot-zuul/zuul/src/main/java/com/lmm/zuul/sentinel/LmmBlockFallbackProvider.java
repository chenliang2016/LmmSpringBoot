package com.lmm.zuul.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.BlockResponse;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackProvider;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class LmmBlockFallbackProvider implements ZuulBlockFallbackProvider {


    // you can define route as service level
    @Override
    public String getRoute() {
        return "/v1/*";
    }

    @Override
    public BlockResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof BlockException) {
            return new BlockResponse(429, "Sentinel block exception", route);
        } else {
            return new BlockResponse(500, "System Error", route);
        }
    }
}
