package com.lmm.zuul.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class GatewayRuleConfig {
    private static final int URL_MATCH_STRATEGY_EXACT = 0;
    private static final int URL_MATCH_STRATEGY_PREFIX = 1;
    private static final int URL_MATCH_STRATEGY_REGEX = 2;

    @PostConstruct
    public void doInit() {
        // Prepare some gateway rules and API definitions (only for demo).
        // It's recommended to leverage dynamic data source or the Sentinel dashboard to push the rules.
        initCustomizedApis(); // 定义接口
        initGatewayRules();// 自定义规则
        initFallBack();// 自定义返回值
    }

    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition apiV1 = new ApiDefinition("api_v1")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/v1/**")
                            .setMatchStrategy(URL_MATCH_STRATEGY_PREFIX));
                }});
        definitions.add(apiV1);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("api_v1")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(50)
                .setIntervalSec(1)
        );
        GatewayRuleManager.loadRules(rules);
    }

    private void initFallBack(){
        ZuulBlockFallbackManager.registerProvider(new LmmBlockFallbackProvider());
    }

}
