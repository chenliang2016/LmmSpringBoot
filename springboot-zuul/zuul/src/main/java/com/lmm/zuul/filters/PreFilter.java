package com.lmm.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreFilter extends ZuulFilter {


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
 
  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

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
//      throw new SignException("miss sign [" + request.getRequestURI() + "]");
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
    String sign2 = DigestUtils.md5Hex(signString);

    if (Objects.equals(sign, sign2)) {
      return true;
    } else {
//      throw new SignException("error sign [" + request.getRequestURI() + "]");
    }
 
    System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
    return null;
  }

  private boolean excludeKey(String key) {
    return StringUtils.isEmpty(key) || EXCLUED_KEYS.contains(key);
  }
}