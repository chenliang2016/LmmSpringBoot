package com.lmm.zuul.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * Result Code Enum
 *
 * @author william
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception"),

    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Request Unauthorized"),

    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found"),

    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read"),

    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported"),

    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported"),

    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "Request Rejected"),

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),

    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter"),

    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch"),

    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error"),

    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Validation Error"),
    //支付通道未找到
    PAY_CHANNEL_NOTFOUND(9001, "PayChannel Not Found"),
    DEVICE_NOTFOUND(9002, "Device Not Found"),


    ERROR_USER(1001,"ERROR_USER"),
    SCORE_NOT_ENOUGH(1002, "Score Not Enough"),
    TRADE_REPEAT(1002, "TradeNo Repeat"),
    TRADE_NOT_FOUND(1003, "TradeNo Not Found"),
    SIGN_ERROR(6001, "签名错误"),
    QUERY_FAIL(2001, "Query Fail");



    final int code;

    final String msg;
}
