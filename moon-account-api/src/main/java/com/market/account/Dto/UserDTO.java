package com.market.account.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    // 账户id
    private Integer accountId;
    // 用户id
    private Integer userId;
    // 昵称
    private String nickName;
    // 头像
    private String userPort;
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
    // 用户积分
    private Long userPoint;
    // 等级积分
    private Long userLevelPoint;
    // 是否实名认证
    private Boolean isIdentity;
    // 创建时间
    private Long createTime;
    // 是否是商家
    private Boolean isBusiness;
    // 是否注销
    private Boolean isDelete;

}
