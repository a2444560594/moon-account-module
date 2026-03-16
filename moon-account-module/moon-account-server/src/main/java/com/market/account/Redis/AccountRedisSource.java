package com.market.account.Redis;

import com.market.account.Config.UserConfig;
import com.market.account.Wrapper.JedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AccountRedisSource {

    // 账户信息缓存
    private static final String STRING_ACCOUNT_CACHE = "MOON_MARKET:account_cache_%s";
    // 账户信息缓存
    private static final String STRING_ACCOUNT_CONTACT_CACHE = "MOON_MARKET:account_cache_%s_%s";
    // 账户注册信息列表缓存
    private static final String REGISTER_ACCOUNTS_CACHE = "MOON_MARKET:register_accounts_cache_%s";

    // 查询防击穿锁
    private static final String STRING_SEARCH_ACCOUNT_LOCK = "MOON_MARKET:search_account_lock";


    private static final int SEARCH_LOCK_EXPIRE_TIME = 5;

    @Autowired
    JedisWrapper jedisWrapper;

    @Autowired
    UserConfig userConfig;

    public String getAccountCacheByAccountId(int accountId) {
        String key = String.format(STRING_ACCOUNT_CACHE, accountId);
        return jedisWrapper.STRING.get(key);
    }

    public void setAccountCacheByAccountId(int accountId, String accountJsonStr) {
        String key = String.format(STRING_ACCOUNT_CACHE, accountId);
        jedisWrapper.STRING.setex(key, accountJsonStr, userConfig.getCacheExpireTime());
    }

    public String getAccountCacheByContact(int contactType, String contact) {
        String key = String.format(STRING_ACCOUNT_CONTACT_CACHE, contactType, contact);
        return jedisWrapper.STRING.get(key);
    }

    public void setAccountCacheByContact(int contactType, String contact, String accountJsonStr) {
        String key = String.format(STRING_ACCOUNT_CONTACT_CACHE, contactType, contact);
        jedisWrapper.STRING.setex(key, accountJsonStr, userConfig.getCacheExpireTime());
    }

    public Set<String> smembersRegisterAccounts(int contactType) {
        String key = String.format(REGISTER_ACCOUNTS_CACHE, contactType);
        return jedisWrapper.SET.smembers(key);
    }

    public void setRegisterAccounts(int contactType, Set<String> contactSet) {
        String key = String.format(REGISTER_ACCOUNTS_CACHE, contactType);
        jedisWrapper.SET.sadd(key, contactSet);
        jedisWrapper.KEY.expire(key, userConfig.getCacheExpireTime());
    }

    public boolean setSearchAccountLock() {
        Long value = jedisWrapper.STRING.setnx(STRING_SEARCH_ACCOUNT_LOCK, "lock");
        jedisWrapper.KEY.expire(STRING_SEARCH_ACCOUNT_LOCK, SEARCH_LOCK_EXPIRE_TIME);
        return value != null && value > 0;
    }
}
