package com.market.account.Redis;

import com.market.account.Wrapper.JedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LevelRedisSource {

    private static final String HASH_LEVEL_CACHE = "MOON_MARKET:level_cache";

    private static final int FIVE_MINUTE = 60 * 5;

    @Autowired
    JedisWrapper jedisWrapper;

    public String getLevelCacheByLevel(int level) {
        return jedisWrapper.HASH.hget(HASH_LEVEL_CACHE, String.valueOf(level));
    }

    public void setLevelCacheByLevel(int level, String levelJsonStr) {
        jedisWrapper.HASH.hset(HASH_LEVEL_CACHE, String.valueOf(level), levelJsonStr);
        jedisWrapper.KEY.expire(HASH_LEVEL_CACHE, FIVE_MINUTE);
    }

    public Map<String, String> getLevelCache() {
        return jedisWrapper.HASH.hgetAll(HASH_LEVEL_CACHE);
    }

    public void setLevelCache(Map<String, String> levelMap) {
        jedisWrapper.HASH.hset(HASH_LEVEL_CACHE, levelMap);
        jedisWrapper.KEY.expire(HASH_LEVEL_CACHE, FIVE_MINUTE);
    }
}
