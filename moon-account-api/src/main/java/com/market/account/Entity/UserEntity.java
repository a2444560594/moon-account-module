package com.market.account.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    // 账户id
    private Integer accountId;
    // 用户id
    private Integer userId;
    // 昵称
    private String nickName;
    // 头像
    private String userAvatar;
    // 手机号
    private String userPhone;
    // 年龄
    private String age;
    // 性别
    private String sex;
    // 生日
    private Long birthTime;
    // 身份证号码
    private String identifyNo;
    // 地址
    private String address;
    // 余额
    private Long pMoney;
    // 等级
    private Long userLevel;
    // 是否实名认证
    private Boolean isIdentity;
    // 创建时间
    private Long createTime;
    // 是否是商家
    private Boolean isBusiness;
    // 是否注销
    private Boolean isDelete;
}
