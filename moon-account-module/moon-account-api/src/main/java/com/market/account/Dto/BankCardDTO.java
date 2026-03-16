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
public class BankCardDTO {

    // 用户id
    private Integer userId;
    // 银行名称
    private String bankName;
    // 银行卡号
    private String bankCardNo;
    // 银行卡姓名
    private String bankCardName;
    // 银行卡手机号
    private String bankCardPhone;
    // 注册银行卡所在地址
    private String bankCardAddress;
    // 银行卡使用状态
    private String bankCardState;
    // 银行卡类型1.借记卡2.信用卡
    private Integer bankCardType;
    // 绑定时间
    private Long bindingTime;

}
