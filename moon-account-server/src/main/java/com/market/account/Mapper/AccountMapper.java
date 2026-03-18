package com.market.account.Mapper;

import com.market.account.Dto.AccountDTO;
import com.market.account.Entity.AccountEntity;

import java.util.Set;

public interface AccountMapper {

    // 根据账户id查询账户信息
     AccountDTO getAccountById(int accountId);

    // 根据联系方式查询账户信息
     AccountDTO getAccountByContact(int contactType , String accountContact);

    // 根据注册联系方式查询账户信息
     Set<String> getAllAccountsByContactType(int contactType);

    // 根据登录账户和密码查询账户信息
     AccountDTO getAccountByLoginAccount(int loginType, String loginKey, String loginValue);

    // 注册账户
     void registerAccount(AccountEntity accountEntity);

    // 修改账户信息
     int updateAccount(AccountEntity accountEntity);


}
