package com.market.account.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {

    private int accountId;
    private String loginAccount;
    private String loginPassword;
    private String loginIp;
    private String registerIp;
    private String registerPhone;
    private String registerEmail;
    private Long registerTime;
    private Long lastLoginTime;

}
