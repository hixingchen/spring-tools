package com.craft.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 认证相关 401x
    UNAUTHORIZED(401, "未登录或登录已过期"),
    TOKEN_INVALID(4011, "Token 无效"),
    ACCOUNT_DISABLED(4012, "账号已被禁用"),

    // 权限相关 403x
    FORBIDDEN(403, "无权限访问"),

    // 参数相关 400x
    PARAM_ERROR(400, "参数错误"),
    PARAM_MISS(4001, "缺少必要参数"),
    PARAM_TYPE_ERROR(4002, "参数类型错误"),

    // 业务相关 50xx
    DATA_NOT_FOUND(5001, "数据不存在"),
    DATA_ALREADY_EXIST(5002, "数据已存在"),
    OPERATION_TOO_FREQUENT(5003, "操作过于频繁");

    private final int code;
    private final String message;
}
