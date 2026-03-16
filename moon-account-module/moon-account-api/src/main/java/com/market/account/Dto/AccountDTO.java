package com.market.account.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDTO {

    // 账户id
    private Integer accountId;
    // 登录账户
    private String registerAccount;
    // 密码
    private String password;
    // 登录ip
    private String loginIp;
    // 登录省份
    private String loginProvince;
    // 登录城市
    private String loginCity;
    // 上一次登录时间
    private String lastLoginTime;
    // 注册ip
    private String registerIp;
    // 注册手机号
    private String registerPhone;
    // 注册邮箱
    private String registerEmail;
    // 注册时间
    private Long registerTime;
    // 注册省份
    private Long registerProvince;
    // 注册城市
    private Long registerCity;
    // token
    private String token;
    // 账户下用户信息
    private UserDTO userDTO;

}
