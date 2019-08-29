package com.lmm.zuul.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmm.zuul.api.BaseResponse;
import com.lmm.zuul.api.ResultCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseFilter extends ZuulFilter {
    protected static UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    protected String getTokenFromRequest(String headerName, HttpServletRequest request) {
        String token = request.getHeader(headerName);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(headerName);
        }
        return token;
    }


    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    protected void setFailedRequest(String body, ResultCode code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 返回错误码
        ctx.setResponseStatusCode(code.getCode());
        ctx.addZuulResponseHeader("Content-Type", "application/json;charset=UTF-8");
        if (ctx.getResponseBody() == null) {
            // 返回错误内容
            BaseResponse baseResponse  = BaseResponse
                    .builder()
                    .code(code)
                    .message(body)
                    .build();

            ctx.setResponseBody( JSONObject.toJSONString(baseResponse));

            // 过滤该请求，不对其进行路由
//            ctx.setSendZuulResponse(false);
        }
    }


}
