package com.yangyongqiang.frailty.biz.util;

import com.yangyongqiang.frailty.common.util.RetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Slf4j
@Component
public class RedisClient {
    private static final int retries = 3;
    public static final long OK_RESULT = 1;
    public static final long NOT_OK_RESULT = 0;

    @Resource(name = "basicJedisPool")
    private JedisPool jedisPool;

    private RetryUtil.RetryPolicy retryNTimes = new RetryUtil.RetryNTimes(retries, 100);

    /**
     * 执行Redis操作，失败重试{@link #retries }次
     *
     * @param func redis 操作
     * @param <T>  返回类型
     * @return redis操作返回结果
     */
    private <T> T doRedisAction(Function<Jedis, T> func) {
        try {
            return RetryUtil.retryOnThrowable(JedisException.class, retryNTimes, () -> {
                Jedis jedis = null;
                try {
                    jedis = jedisPool.getResource();
                    return func.apply(jedis);
                } finally {
                    if (jedis != null) {
                        try {
                            jedis.close();
                        } catch (Exception e) {
                            log.warn("jedis close failed, ", e);
                        }
                    }
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException("Error while do redis action: " + e.getMessage(), e);
        }
    }

    // ========= KEY 操作

    /**
     * @see Jedis#exists(String)
     */
    public Boolean exists(String key) {
        return doRedisAction(jedis -> jedis.exists(key));
    }

    /**
     * @see Jedis#del(String...)
     */
    public Long del(String... keys) {
        return doRedisAction(jedis -> jedis.del(keys));
    }

    /**
     * @see Jedis#del(String)
     */
    public Long del(String key) {
        return doRedisAction(jedis -> jedis.del(key));
    }

    /**
     * @see Jedis#type(String)
     */
    public String type(String key) {
        return doRedisAction(jedis -> jedis.type(key));
    }

    /**
     * @see Jedis#keys(String)
     */
    public Set<String> keys(String pattern) {
        return doRedisAction(jedis -> jedis.keys(pattern));
    }

    /**
     * @see Jedis#randomKey()
     */
    public String randomKey() {
        return doRedisAction(jedis -> jedis.randomKey());
    }

    /**
     * @see Jedis#rename(String, String)
     */
    public String rename(String oldKey, String newKey) {
        return doRedisAction(jedis -> jedis.rename(oldKey, newKey));
    }

    /**
     * @see Jedis#renamenx(String, String)
     */
    public Long renamenx(String oldKey, String newKey) {
        return doRedisAction(jedis -> jedis.renamenx(oldKey, newKey));
    }

    /**
     * @see Jedis#expire(String, int)
     */
    public Long expire(String key, int seconds) {
        return doRedisAction(jedis -> jedis.expire(key, seconds));
    }

    /**
     * @see Jedis#expireAt(String, long)
     */
    public Long expireAt(String key, long unixTime) {
        return doRedisAction(jedis -> jedis.expireAt(key, unixTime));
    }

    /**
     * @see Jedis#ttl(String)
     */
    public Long ttl(String key) {
        return doRedisAction(jedis -> jedis.ttl(key));
    }

    /**
     * @see Jedis#move(String, int)
     */
    public Long move(String key, int dbIndex) {
        return doRedisAction(jedis -> jedis.move(key, dbIndex));
    }

    /**
     * @see Jedis#getSet(String, String)
     */
    public String getSet(String key, String value) {
        return doRedisAction(jedis -> jedis.getSet(key, value));
    }

    /**
     * @see Jedis#setnx(String, String)
     */
    public Long setnx(String key, String value) {
        return doRedisAction(jedis -> jedis.setnx(key, value));
    }

    /**
     * @see Jedis#setex(String, int, String)
     */
    public String setex(String key, int seconds, String value) {
        return doRedisAction(jedis -> jedis.setex(key, seconds, value));
    }

    /**
     * @see Jedis#mset(String...)
     */
    public String mset(String... keysvalues) {
        return doRedisAction(jedis -> jedis.mset(keysvalues));
    }

    /**
     * @see Jedis#decrBy(String, long)
     */
    public Long decrBy(String key, long integer) {
        return doRedisAction(jedis -> jedis.decrBy(key, integer));
    }

    /**
     * @see Jedis#decr(String)
     */
    public Long decr(String key) {
        return doRedisAction(jedis -> jedis.decr(key));
    }

    /**
     * @see Jedis#incrBy(String, long)
     */
    public Long incrBy(String key, Long integer) {
        return doRedisAction(jedis -> jedis.incrBy(key, integer));
    }

    /**
     * @see Jedis#incrByFloat(String, double)
     */
    public Double incrByFloat(String key, double value) {
        return doRedisAction(jedis -> jedis.incrByFloat(key, value));
    }

    /**
     * @see Jedis#incr(String)
     */
    public Long incr(String key) {
        return doRedisAction(jedis -> jedis.incr(key));
    }

    /**
     * @see Jedis#append(String, String)
     */
    public Long append(String key, String value) {
        return doRedisAction(jedis -> jedis.append(key, value));
    }

    /**
     * @see Jedis#substr(String, int, int)
     */
    public String substr(String key, int start, int end) {
        return doRedisAction(jedis -> jedis.substr(key, start, end));
    }

    /**
     * @param expireTime 单位Second
     */
    public long setnx(String key, String value, Long expireTime) {
        if ("OK".equalsIgnoreCase(set(key, value, "NX", "EX", expireTime))) {
            return OK_RESULT;
        }
        return NOT_OK_RESULT;
    }

    // ========= String 操作

    /**
     * @see Jedis#set(String, String)
     */
    public String set(String key, String value) {
        return doRedisAction(jedis -> jedis.set(key, value));
    }

    /**
     * @see Jedis#set(String, String, String, String, long)
     */
    public String set(String key, String value, String nxxx, String expx, long time) {
        return doRedisAction(jedis -> jedis.set(key, value, nxxx, expx, time));
    }

    /**
     * @see Jedis#get(String)
     */
    public String get(String key) {
        return doRedisAction(jedis -> jedis.get(key));
    }

    // ========= Hash 操作


    /**
     * @see Jedis#hset(String, String, String)
     */
    public Long hset(final String key, final String field, final String value) {
        return doRedisAction(jedis -> jedis.hset(key, field, value));
    }

    /**
     * @see Jedis#hget(String, String)
     */
    public String hget(final String key, final String field) {
        return doRedisAction(jedis -> jedis.hget(key, field));
    }

    /**
     * @see Jedis#hsetnx(String, String, String)
     */
    public Long hsetnx(final String key, final String field, final String value) {
        return doRedisAction(jedis -> jedis.hsetnx(key, field, value));
    }

    /**
     * @see Jedis#hmset(String, Map)
     */
    public String hmset(final String key, final Map<String, String> hash) {
        return doRedisAction(jedis -> jedis.hmset(key, hash));
    }

    /**
     * @see Jedis#hmget(String, String...)
     */
    public List<String> hmget(final String key, final String... fields) {
        return doRedisAction(jedis -> jedis.hmget(key, fields));
    }

    /**
     * @see Jedis#hincrBy(String, String, long)
     */
    public Long hincrBy(final String key, final String field, final long value) {
        return doRedisAction(jedis -> jedis.hincrBy(key, field, value));
    }

    /**
     * @see Jedis#hincrByFloat(String, String, double)
     */
    public Double hincrByFloat(final String key, final String field, final double value) {
        return doRedisAction(jedis -> jedis.hincrByFloat(key, field, value));
    }

    /**
     * @see Jedis#hexists(String, String)
     */
    public Boolean hexists(final String key, final String field) {
        return doRedisAction(jedis -> jedis.hexists(key, field));
    }

    /**
     * @see Jedis#hdel(String, String...)
     */
    public Long hdel(final String key, final String... fields) {
        return doRedisAction(jedis -> jedis.hdel(key, fields));
    }

    /**
     * @see Jedis#hlen(String)
     */
    public Long hlen(final String key) {
        return doRedisAction(jedis -> jedis.hlen(key));
    }

    /**
     * @see Jedis#hkeys(String)
     */
    public Set<String> hkeys(final String key) {
        return doRedisAction(jedis -> jedis.hkeys(key));
    }

    /**
     * @see Jedis#hvals(String)
     */
    public List<String> hvals(final String key) {
        return doRedisAction(jedis -> jedis.hvals(key));
    }

    /**
     * @see Jedis#hgetAll(String)
     */
    public Map<String, String> hgetAll(final String key) {
        return doRedisAction(jedis -> jedis.hgetAll(key));
    }

    // ========= List 操作

    /**
     * @see Jedis#rpush(String, String...)
     */
    public Long rpush(String key, String... strings) {
        return doRedisAction(jedis -> jedis.rpush(key, strings));
    }

    /**
     * @see Jedis#lpush(String, String...)
     */
    public Long lpush(String key, String... strings) {
        return doRedisAction(jedis -> jedis.lpush(key, strings));
    }

    /**
     * @see Jedis#llen(String)
     */
    public Long llen(String key) {
        return doRedisAction(jedis -> jedis.llen(key));
    }

    /**
     * @see Jedis#lrange(String, long, long)
     */
    public List<String> lrange(String key, long start, long end) {
        return doRedisAction(jedis -> jedis.lrange(key, start, end));
    }

    /**
     * @see Jedis#ltrim(String, long, long)
     */
    public String ltrim(String key, long start, long end) {
        return doRedisAction(jedis -> jedis.ltrim(key, start, end));
    }

    /**
     * @see Jedis#lindex(String, long)
     */
    public String lindex(String key, long index) {
        return doRedisAction(jedis -> jedis.lindex(key, index));
    }

    /**
     * @see Jedis#lset(String, long, String)
     */
    public String lset(String key, long index, String value) {
        return doRedisAction(jedis -> jedis.lset(key, index, value));
    }

    /**
     * @see Jedis#lrem(String, long, String)
     */
    public Long lrem(String key, long count, String value) {
        return doRedisAction(jedis -> jedis.lrem(key, count, value));
    }

    /**
     * @see Jedis#lpop(String)
     */
    public String lpop(String key) {
        return doRedisAction(jedis -> jedis.lpop(key));
    }

    /**
     * @see Jedis#rpop(String)
     */
    public String rpop(String key) {
        return doRedisAction(jedis -> jedis.rpop(key));
    }

    /**
     * @see Jedis#rpoplpush(String, String)
     */
    public String rpoplpush(String srckey, String dstkey) {
        return doRedisAction(jedis -> jedis.rpoplpush(srckey, dstkey));
    }

    public Long sadd(String key, String... member) {
        return doRedisAction(jedis -> jedis.sadd(key, member));
    }

    public Long srem(String key, String... member) {
        return doRedisAction(jedis -> jedis.srem(key, member));
    }

    public Set<String> smembers(String key) {
        return doRedisAction(jedis -> jedis.smembers(key));
    }

    public ScanResult<String> scan(final String cursor) {
        return doRedisAction(jedis -> jedis.scan(cursor));
    }

    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        return doRedisAction(jedis -> jedis.scan(cursor, params));
    }

    public String spop(String key) {
        return doRedisAction(jedis -> jedis.spop(key));
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return doRedisAction(jedis -> jedis.hscan(key, cursor));
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor,
                                                       final ScanParams params) {
        return doRedisAction(jedis -> jedis.hscan(key, cursor, params));
    }

    public ScanResult<String> sscan(final String key, final String cursor) {
        return doRedisAction(jedis -> jedis.sscan(key, cursor));
    }

    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        return doRedisAction(jedis -> jedis.sscan(key, cursor, params));
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return doRedisAction(jedis -> jedis.zscan(key, cursor));
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        return doRedisAction(jedis -> jedis.zscan(key, cursor, params));
    }
}