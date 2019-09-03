package com.lmm.zuul.filters;

import com.alibaba.fastjson.JSONObject;
import com.lmm.zuul.api.BaseResponse;
import com.netflix.zuul.ZuulFilter;

public class ErrorFilter extends ZuulFilter {
 
  @Override
  public String filterType() {
    return "error";
  }
 
  @Override
  public int filterOrder() {
    return 1;
  }
 
  @Override
  public boolean shouldFilter() {
    return true;
  }
 
  @Override
  public Object run() {
   System.out.println("Inside Route Filter");

//    BaseResponse baseResponse  = BaseResponse
//            .builder()
//            .code(code)
//            .message(body)
//            .build();
//
//    ctx.setResponseBody( JSONObject.toJSONString(baseResponse));
 
    return null;
  }
}
