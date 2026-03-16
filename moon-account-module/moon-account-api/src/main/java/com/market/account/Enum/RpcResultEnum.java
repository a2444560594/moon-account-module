package com.market.account.Enum;

import lombok.Getter;

@Getter
public enum RpcResultEnum {

    SUCCESS(200, "成功"),
    FAIL(400, "服务器出错，请稍后尝试"),

    NOT_NULL(1001, "参数[%s]不能为空"),
    NOT_FIND_ACCOUNT(1002, "未找到该账户"),
    TYPE_NOT_EXIST(1003, "参数类型不存在"),
    USERNAME_EXISTS(1006, "用户名已存在"),
    PHONE_EXISTS(1007, "手机号已存在"),
    EMAIL_EXISTS(1008, "邮箱已存在"),
    UNKNOWN_ERROR(1009, "未知错误"),
    NOT_FIND_USER(1010, "未找到用户"),
    NOT_FIND_LEVEL(1011, "未找到等级"),
    ADD_LEVEL_POINT_LESS_ZERO(1012, "添加用户等级积分小于0");

    final int code;
    final String message;

    RpcResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
