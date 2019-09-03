package com.lmm.zuul.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmm.zuul.api.ResultCode;
import com.lmm.zuul.filters.utils.RequestWrapper;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignFilter extends BaseFilter {


  @Value("${app.secret}")
  private String appSecret;
  @Value("${app.sign-skip}")
  private Boolean signSkip;

  private List<String> EXCLUED_KEYS = Collections.singletonList("sign");

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  /**
   * 判断本次请求的数据类型是否为json
   *
   * @param request request
   * @return true: 是 JSON 数据; false: 非 json 数据
   */
  private boolean isJson(HttpServletRequest request) {
    if (request.getContentType() != null) {
      return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
              request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    return false;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    ServletRequest requestWrapper = null;
    if(request instanceof HttpServletRequest) {
      requestWrapper = new RequestWrapper(request);
    }

    Map<String,String[]> p =  request.getParameterMap();

    String contentType =  ctx.getRequest().getHeader("Content-Type");

    if(request.getMethod().toUpperCase().equals("GET")){
      return requestByForm(request);
    }else{
      if (contentType.contains("application/json")){
        return requestByForm(request);
      }else{
        return requestBody(requestWrapper);
      }
    }

  }

  public Object requestByForm(HttpServletRequest request){

    Map<String, String[]> params = request.getParameterMap();


    if ("/user/check_verify".equals(request.getServletPath())) {
      return true;
    }
    if ("/error".equals(request.getServletPath())) {
      return true;
    }

    if ("/rb/result".equals(request.getServletPath())) {
      return true;
    }

    if ( ArrayUtils.isNotEmpty(params.get("_xyz"))) {
      return true;
    }

    String[] signs = params.get("sign");
    if (ArrayUtils.isEmpty(signs)) {
      setFailedRequest("签名错误", ResultCode.SIGN_ERROR);
    }
    String sign = signs[0];

    String[] keys = params.keySet().toArray(new String[]{});
    Arrays.sort(keys);

    String[] values = Stream.of(keys).map(key -> {
      if (!excludeKey(key) && ArrayUtils.isNotEmpty(params.get(key))) {
        return key + "=" + params.get(key)[0];
      } else {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList()).toArray(new String[]{});

    String signString = String.join("&", values) + appSecret;
    String sign2 = DigestUtils.md5Hex(signString).toUpperCase();

    if (Objects.equals(sign, sign2)) {
      return true;
    } else {
      setFailedRequest("签名错误", ResultCode.SIGN_ERROR);
    }

    System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
    return null;
  }

  public Object requestBody(ServletRequest requestWrapper){
    Map<String, Object> params;
    RequestWrapper requestWrapper1 = (RequestWrapper)requestWrapper;
    params = JSONObject.parseObject(requestWrapper1.getBody(),Map.class);

    String signs = params.get("sign").toString();
    if (StringUtils.isEmpty(signs)) {
      setFailedRequest("签名错误", ResultCode.SIGN_ERROR);
    }
    String sign = signs;

    String[] keys = params.keySet().toArray(new String[]{});
    Arrays.sort(keys);

    String[] values = Stream.of(keys).map(key -> {
      if (!excludeKey(key) && StringUtils.isNotEmpty(params.get(key).toString())) {
        return key + "=" + params.get(key);
      } else {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList()).toArray(new String[]{});

    String signString = String.join("&", values) + appSecret;
    String sign2 = DigestUtils.md5Hex(signString).toUpperCase();

    if (Objects.equals(sign, sign2)) {
      return true;
    } else {
      setFailedRequest("签名错误", ResultCode.SIGN_ERROR);
    }

    return null;
  }

  private boolean excludeKey(String key) {
    return StringUtils.isEmpty(key) || EXCLUED_KEYS.contains(key);
  }
}