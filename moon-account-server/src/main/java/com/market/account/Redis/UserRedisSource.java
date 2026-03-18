package com.market.account.Redis;

import com.market.account.Config.UserConfig;
import com.market.account.Dto.UserDTO;
import com.market.account.Wrapper.JedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserRedisSource {

    private static final String STRING_USER_CACHE = "MOON_MARKET:user_cache_%s";

    private static final String STRING_USER_ACCOUNT_CACHE = "MOON_MARKET:user_account_cache_%s";

    private static final String STRING_USER_LEVEL_CACHE = "MOON_MARKET:user_level_cache_%s";

    private static final int FIVE_MINUTE = 60 * 5;

    @Autowired
    JedisWrapper jedisWrapper;

    @Autowired
    UserConfig userConfig;


    public String getUserCacheByUserId(int userId) {
        String key = String.format(STRING_USER_CACHE, userId);
        return jedisWrapper.STRING.get(key);
    }

    public void setUserCacheByUserId(int userId, String userJsonStr) {
        String key = String.format(STRING_USER_CACHE, userId);
        jedisWrapper.STRING.setex(key, userJsonStr, userConfig.getCacheExpireTime());
    }

    public void removeUserCacheByUserId(int userId) {
        String key = String.format(STRING_USER_CACHE, userId);
        jedisWrapper.KEY.del(key);
    }

    public void removeUserCacheByUserIds(List<Integer> userIds) {
        String[] removeCacheKeys = userIds.stream().map(userId -> String.format(STRING_USER_CACHE, userId)).toArray(String[]::new);
        jedisWrapper.KEY.del(removeCacheKeys);
    }

    public String getUserCacheByAccountId(int accountId) {
        String key = String.format(STRING_USER_ACCOUNT_CACHE, accountId);
        return jedisWrapper.STRING.get(key);
    }

    public void setUserCacheByAccountId(int accountId, String accountJsonStr) {
        String key = String.format(STRING_USER_ACCOUNT_CACHE, accountId);
        jedisWrapper.STRING.setex(key, accountJsonStr, userConfig.getCacheExpireTime());
    }

    public void removeUserCacheByAccountId(int accountId) {
        String key = String.format(STRING_USER_ACCOUNT_CACHE, accountId);
        jedisWrapper.KEY.del(key);
    }

    public void removeUserCacheByAccountIds(List<Integer> accountIds) {
        String[] removeCacheKeys = accountIds.stream().map(accountId -> String.format(STRING_USER_ACCOUNT_CACHE, accountId)).toArray(String[]::new);
        jedisWrapper.KEY.del(removeCacheKeys);
    }

    public List<String> getUserCacheByLevel(int level) {
        String key = String.format(STRING_USER_LEVEL_CACHE, level);
        return jedisWrapper.HASH.hvals(key);
    }

    public void setUserCacheByLevel(int level, Map<String, String> userMap) {
        String key = String.format(STRING_USER_LEVEL_CACHE, level);
        jedisWrapper.HASH.hset(key, userMap);
        jedisWrapper.KEY.expire(key, userConfig.getCacheExpireTime());
    }

    public void removeUserCacheByLevel(int level) {
        String key = String.format(STRING_USER_LEVEL_CACHE, level);
        jedisWrapper.KEY.del(key);
    }

    public List<String> getUserFollows(int userId) {
        String key = String.format("MOON_MARKET:user_follows_%s", userId);
        return jedisWrapper.LIST.lrange(key, 0, -1);
    }

    public void setUserFollows(int userId, List<String> follows) {
        String key = String.format("MOON_MARKET:user_follows_%s", userId);
        jedisWrapper.LIST.lpush(key, follows.toArray(String[]::new));
        jedisWrapper.KEY.expire(key, userConfig.getCacheExpireTime());
    }

}
