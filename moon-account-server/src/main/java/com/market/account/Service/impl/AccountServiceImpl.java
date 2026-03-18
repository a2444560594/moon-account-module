package com.market.account.Service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.market.account.Dto.AccountDTO;
import com.market.account.Dto.UserDTO;
import com.market.account.Entity.AccountEntity;
import com.market.account.Enum.ContactTypeEnum;
import com.market.account.Enum.RpcResultEnum;
import com.market.account.Exception.BusinessException;
import com.market.account.Mapper.AccountMapper;
import com.market.account.Mapper.BankMapper;
import com.market.account.Mapper.UserMapper;
import com.market.account.Redis.AccountRedisSource;
import com.market.account.Service.AccountService;
import com.market.account.Vo.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@DubboService(version = "1.0.0")
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BankMapper bankMapper;

    @Autowired
    AccountRedisSource accountRedisSource;

    @Override
    public RpcResult<AccountDTO> getAccountByAccountId(int accountId) {
        try {
            log.info("根据账户id查找账户信息！accountId:{}", accountId);
            AccountDTO accountDTO;
            // 缓存中获取账户信息
            String accountJsonStr = accountRedisSource.getAccountCacheByAccountId(accountId);
            if (accountJsonStr != null) {
                accountDTO = JSONObject.parseObject(accountJsonStr, AccountDTO.class);
                return RpcResult.success(accountDTO);
            }

            // 获取账户信息
            accountDTO = accountMapper.getAccountById(accountId);
            if (accountDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_ACCOUNT.getCode(), RpcResultEnum.NOT_FIND_ACCOUNT.getMessage());
            }

            // 获取用户信息
            UserDTO userDTO = userMapper.getUserByAccountId(accountId);
            if (userDTO == null) {
                return RpcResult.success(accountDTO);
            }

            accountDTO.setUserDTO(userDTO);
            // 缓存账户信息
            accountJsonStr = JSONObject.toJSONString(accountDTO);
            accountRedisSource.setAccountCacheByAccountId(accountId, accountJsonStr);

            return RpcResult.success(accountDTO);
        } catch (Exception e) {
            log.error("查找账户信息失败！accountId:{} ,error:{}", accountId, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<AccountDTO> getAccountByContact(int contactType, String contact) {
        try {
            log.info("根据联系方式查找账户信息！contactType:{} ,contact:{}", contactType, contact);
            if (ContactTypeEnum.contains(contactType)) {
                return RpcResult.fail(RpcResultEnum.TYPE_NOT_EXIST.getCode(), RpcResultEnum.TYPE_NOT_EXIST.getMessage());
            }

            if (contact == null || contact.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "contact"));
            }

            AccountDTO accountDTO;
            // 缓存中获取账户信息
            String accountJsonStr = accountRedisSource.getAccountCacheByContact(contactType, contact);
            if (accountJsonStr != null) {
                accountDTO = JSONObject.parseObject(accountJsonStr, AccountDTO.class);
                return RpcResult.success(accountDTO);
            }

            accountDTO = accountMapper.getAccountByContact(contactType, contact);
            if (accountDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_ACCOUNT.getCode(), RpcResultEnum.NOT_FIND_ACCOUNT.getMessage());
            }

            // 获取用户信息
            UserDTO userDTO = userMapper.getUserByAccountId(accountDTO.getAccountId());
            if (userDTO == null) {
                return RpcResult.success(accountDTO);
            }

            accountDTO.setUserDTO(userDTO);
            // 缓存账户信息
            accountJsonStr = JSONObject.toJSONString(accountDTO);
            accountRedisSource.setAccountCacheByContact(contactType, contact, accountJsonStr);

            return RpcResult.success(accountDTO);
        } catch (Exception e) {
            log.error("查找账户信息失败！contactType:{} ,contact:{} ,error:{}", contactType, contact, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<AccountDTO> getAccountByLoginAccount(int loginType, String loginKey, String password) {
        try {
            log.info("根据登录账户查找账户信息！loginType:{} ,loginKey:{} ,password:{}", loginType, loginKey, password);
            if (ContactTypeEnum.contains(loginType)) {
                return RpcResult.fail(RpcResultEnum.TYPE_NOT_EXIST.getCode(), RpcResultEnum.TYPE_NOT_EXIST.getMessage());
            }

            if (loginKey == null || loginKey.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "loginKey"));
            }

            if (password == null || password.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "password"));
            }

            AccountDTO accountDTO = accountMapper.getAccountByLoginAccount(loginType, loginKey, password);
            if (accountDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_ACCOUNT.getCode(), RpcResultEnum.NOT_FIND_ACCOUNT.getMessage());
            }

            return getAccountByAccountId(accountDTO.getAccountId());
        } catch (Exception e) {
            log.error("查找账户信息失败！loginType:{} ,loginKey:{} ,password:{} ,error:{}", loginType, loginKey, password, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Set<String>> getAllAccountsByContactType(int contactType) {
        try {
            log.info("获取注册联系方式！contactType:{}", contactType);
            if (ContactTypeEnum.contains(contactType)) {
                return RpcResult.fail(RpcResultEnum.TYPE_NOT_EXIST.getCode(), RpcResultEnum.TYPE_NOT_EXIST.getMessage());
            }

            Set<String> registerAccountSet = accountRedisSource.smembersRegisterAccounts(contactType);
            if (registerAccountSet == null) {
                boolean isGetLock = accountRedisSource.setSearchAccountLock();
                if (isGetLock) {
                    registerAccountSet = accountMapper.getAllAccountsByContactType(contactType);
                    accountRedisSource.setRegisterAccounts(contactType, registerAccountSet);
                    return RpcResult.success(registerAccountSet);
                }

                int retryCount = 0;
                while (retryCount < 5) {
                    registerAccountSet = accountRedisSource.smembersRegisterAccounts(contactType);
                    if (registerAccountSet != null) {
                        break;
                    }
                    retryCount++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 恢复中断状态
                        break;
                    }
                }
            }

            return RpcResult.success(registerAccountSet);
        } catch (Exception e) {
            log.error("获取所有注册联系方式失败！contactType:{} ,error:{}", contactType, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Boolean> registerAccount(AccountEntity accountEntity) {
        try {
            log.info("注册账户！accountEntity:{}", accountEntity);
            if (accountEntity == null) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "accountEntity"));
            }
            if (accountEntity.getLoginAccount() == null || accountEntity.getLoginAccount().isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "loginAccount"));
            }
            if (accountEntity.getLoginPassword() == null || accountEntity.getLoginPassword().isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "loginPassword"));
            }
            if (accountEntity.getRegisterIp() == null || accountEntity.getRegisterIp().isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "registerIp"));
            }

            accountEntity.setRegisterTime(System.currentTimeMillis());
            accountEntity.setLastLoginTime(System.currentTimeMillis());
            accountMapper.registerAccount(accountEntity);
            return RpcResult.success(true);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(parseDuplicateKeyError(e.getMessage()));
        } catch (Exception e) {
            log.error("注册账户失败！accountEntity:{} ,error:{}", accountEntity, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Boolean> updateAccountPassword(int accountId, String loginPassword) {
        try {
            log.info("修改账户密码！accountId:{} ,loginPassword:{}", accountId, loginPassword);
            if (accountId <= 0) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "accountId"));
            }
            if (loginPassword == null || loginPassword.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "loginPassword"));
            }
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountId(accountId);
            accountEntity.setLoginPassword(loginPassword);
            accountMapper.updateAccount(accountEntity);

            return RpcResult.success(true);
        } catch (Exception e) {
            log.error("修改账户密码失败！accountId:{} ,loginPassword:{} ,error:{}", accountId, loginPassword, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Boolean> updateAccountContact(int contactType, int accountId, String contact) {
        try {
            log.info("修改账户注册联系方式！accountId:{} ,contact:{}", accountId, contact);
            if (!ContactTypeEnum.PHONE.getType().equals(contactType) && !ContactTypeEnum.EMAIL.getType().equals(contactType)) {
                return RpcResult.fail(RpcResultEnum.TYPE_NOT_EXIST.getCode(), RpcResultEnum.TYPE_NOT_EXIST.getMessage());
            }
            if (accountId <= 0) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "accountId"));
            }
            if (contact == null || contact.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_NULL.getCode(), String.format(RpcResultEnum.NOT_NULL.getMessage(), "contact"));
            }

            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountId(accountId);
            if (ContactTypeEnum.PHONE.getType().equals(contactType)) {
                accountEntity.setRegisterPhone(contact);
            } else if (ContactTypeEnum.EMAIL.getType().equals(contactType)) {
                accountEntity.setRegisterEmail(contact);
            }
            accountMapper.updateAccount(accountEntity);
            return RpcResult.success(true);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(parseDuplicateKeyError(e.getMessage()));
        } catch (Exception e) {
            log.error("修改账户注册联系方式失败！accountId:{} ,contact:{} ,error:{}", accountId, contact, e.getMessage());
        }
        return RpcResult.fail();
    }


    private RpcResultEnum parseDuplicateKeyError(String errorMessage) {
        if (errorMessage.contains("uk_loginAccount")) {
            return RpcResultEnum.USERNAME_EXISTS;
        } else if (errorMessage.contains("uk_phone")) {
            return RpcResultEnum.PHONE_EXISTS;
        } else if (errorMessage.contains("uk_email")) {
            return RpcResultEnum.EMAIL_EXISTS;
        } else {
            return RpcResultEnum.UNKNOWN_ERROR;
        }
    }
}
