package com.market.account.Wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class JedisWrapper {

    static JedisPool jedisPool;

    public final KEY KEY = new KEY();
    public final STRING STRING = new STRING();
    public final LIST LIST = new LIST();
    public final SET SET = new SET();
    public final HASH HASH = new HASH();
    public final ZSET ZSET = new ZSET();

    public class KEY {
        public Long del(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.del(key);
            } catch (JedisException e) {
                log.error("del error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Boolean exists(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.exists(key);
            } catch (JedisException e) {
                log.error("exists error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long expire(String key, int seconds) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.expire(key, seconds);
            } catch (JedisException e) {
                log.error("expire error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long ttl(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.ttl(key);
            } catch (JedisException e) {
                log.error("ttl error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Set<String> keys(String pattern) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.keys(pattern);
            } catch (JedisException e) {
                log.error("keys error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long persist(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.persist(key);
            } catch (JedisException e) {
                log.error("persist error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String rename(String oldKey, String newKey) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.rename(oldKey, newKey);
            } catch (JedisException e) {
                log.error("rename error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }
    }

    public class STRING {
        public String get(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            } catch (JedisException e) {
                log.error("get error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String set(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.set(key, value);
            } catch (JedisException e) {
                log.error("set error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String setex(String key, String value, int seconds) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.setex(key, seconds, value);
            } catch (JedisException e) {
                log.error("setex error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String getset(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.getSet(key, value);
            } catch (JedisException e) {
                log.error("getset error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long setnx(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.setnx(key, value);
            } catch (JedisException e) {
                log.error("setnx error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long strlen(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.strlen(key);
            } catch (JedisException e) {
                log.error("strlen error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String mset(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.mset(key, value);
            } catch (JedisException e) {
                log.error("mset error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String mset(String... keysValues) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.mset(keysValues);
            } catch (JedisException e) {
                log.error("mset error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> mget(String... keys) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.mget(keys);
            } catch (JedisException e) {
                log.error("mget error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long incr(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.incr(key);
            } catch (JedisException e) {
                log.error("incr error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long decr(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.decr(key);
            } catch (JedisException e) {
                log.error("decr error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long incrBy(String key, long value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.incrBy(key, value);
            } catch (JedisException e) {
                log.error("incrBy error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long decrBy(String key, long value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.decrBy(key, value);
            } catch (JedisException e) {
                log.error("decrBy error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long append(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.append(key, value);
            } catch (JedisException e) {
                log.error("append error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

    }

    public class LIST {
        public Long lpush(String key, String[] value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lpush(key, value);
            } catch (JedisException e) {
                log.error("lpush error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long lpush(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lpush(key, value);
            } catch (JedisException e) {
                log.error("lpush error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String lpop(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lpop(key);
            } catch (JedisException e) {
                log.error("lpop error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long rpush(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.rpush(key, value);
            } catch (JedisException e) {
                log.error("rpush error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String rpop(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.rpop(key);
            } catch (JedisException e) {
                log.error("rpop error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> brpop(String key, int timeout) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.brpop(timeout, key);
            } catch (JedisException e) {
                log.error("brpop error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> blpop(String key, int timeout) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.blpop(timeout, key);
            } catch (JedisException e) {
                log.error("blpop error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> lrange(String key, long start, long end) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lrange(key, start, end);
            } catch (JedisException e) {
                log.error("lrange error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long llen(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.llen(key);
            } catch (JedisException e) {
                log.error("llen error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long lrem(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lrem(key, 1, value);
            } catch (JedisException e) {
                log.error("lrem error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long lpushx(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.lpushx(key, value);
            } catch (JedisException e) {
                log.error("lpushx error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long rpushx(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.rpushx(key, value);
            } catch (JedisException e) {
                log.error("rpushx error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

    }

    public class SET {

        public Long sadd(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.sadd(key, value);
            } catch (JedisException e) {
                log.error("sadd error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long sadd(String key, Set<String> values) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.sadd(key, values.stream().toArray(String[]::new));
            } catch (JedisException e) {
                log.error("sadd error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long srem(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.srem(key, value);
            } catch (JedisException e) {
                log.error("srem error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long scard(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.scard(key);
            } catch (JedisException e) {
                log.error("scard error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Boolean sismember(String key, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.sismember(key, value);
            } catch (JedisException e) {
                log.error("sismember error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String spop(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.spop(key);
            } catch (JedisException e) {
                log.error("spop error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Set<String> smembers(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.smembers(key);
            } catch (JedisException e) {
                log.error("smembers error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String srandmember(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.srandmember(key);
            } catch (JedisException e) {
                log.error("srandmember error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

    }

    public class HASH {

        public Long hset(String key, Map<String, String> hash) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hset(key, hash);
            } catch (JedisException e) {
                log.error("hmset error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long hset(String key, String field, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hset(key, field, value);
            } catch (JedisException e) {
                log.error("hset error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long hsetnx(String key, String field, String value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hsetnx(key, field, value);
            } catch (JedisException e) {
                log.error("hsetnx error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public String hget(String key, String field) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hget(key, field);
            } catch (JedisException e) {
                log.error("hget error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long hdel(String key, String... fields) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hdel(key, fields);
            } catch (JedisException e) {
                log.error("hdel error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long hlen(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hlen(key);
            } catch (JedisException e) {
                log.error("hlen error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Set<String> hkeys(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hkeys(key);
            } catch (JedisException e) {
                log.error("hkeys error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> hvals(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hvals(key);
            } catch (JedisException e) {
                log.error("hvals error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Map<String, String> hgetAll(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hgetAll(key);
            } catch (JedisException e) {
                log.error("hgetAll error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long hincrBy(String key, String field, long value) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hincrBy(key, field, value);
            } catch (JedisException e) {
                log.error("hincrBy error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Boolean hexists(String key, String field) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hexists(key, field);
            } catch (JedisException e) {
                log.error("hexists error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

    }

    public class ZSET {

        public Long zadd(String key, Map<String, Double> socreMembers) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zadd(key, socreMembers);
            } catch (JedisException e) {
                log.error("zadd error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zadd(String key, double score, String member) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zadd(key, score, member);
            } catch (JedisException e) {
                log.error("zadd error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> zrange(String key, long start, long end) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrange(key, start, end);
            } catch (JedisException e) {
                log.error("zrange error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> zrevrange(String key, long start, long end) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrange(key, start, end);
            } catch (JedisException e) {
                log.error("zrevrange error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zrem(String key, String... members) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrem(key, members);
            } catch (JedisException e) {
                log.error("zrem error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zcard(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zcard(key);
            } catch (JedisException e) {
                log.error("zcard error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Double zscore(String key, String member) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zscore(key, member);
            } catch (JedisException e) {
                log.error("zscore error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zrank(String key, String member) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrank(key, member);
            } catch (JedisException e) {
                log.error("zrank error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zrevrank(String key, String member) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrank(key, member);
            } catch (JedisException e) {
                log.error("zrevrank error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Long zcount(String key, double min, double max) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zcount(key, min, max);
            } catch (JedisException e) {
                log.error("zcount error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public List<String> zrevrangeByScore(String key, double max, double min) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrangeByScore(key, max, min);
            } catch (JedisException e) {
                log.error("zrevrangeByScore error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }

        public Double zincrby(String key, String member, double score) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zincrby(key, score, member);
            } catch (JedisException e) {
                log.error("zincrby error", e);
                return null;
            } catch (Exception e) {
                log.error("jedisWrapper exception err ", e);
                return null;
            }
        }
    }

}
