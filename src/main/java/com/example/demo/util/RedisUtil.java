package com.example.demo.util;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 */
@Component
public final class RedisUtil {

	/**
	 * 默认过期时长，单位：秒
	 */
	public final static long DEFAULT_EXPIRE = 1800;

	@Autowired
	private RedissonClient redissonClient;

	// =============================common============================

	/**
	 * 指定 key 失效时间
	 * @param key 键
	 * @param seconds 时间(秒)
	 * @return
	 */
	public void expire(String key, long seconds) {
		if (seconds > 0) {
			redissonClient.getKeys().expire(key, seconds, TimeUnit.SECONDS);
		}
	}

	/**
	 * 指定 key 默认失效时间
	 * @param key 键
	 * @return
	 */
	public void expireDefault(String key) {
		this.expire(key, DEFAULT_EXPIRE);
	}

	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(毫秒) 返回 -2 : key不存在 -1 ：无限制
	 */
	public long getExpire(String key) {
		return redissonClient.getKeys().remainTimeToLive(key);
	}
	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		return redissonClient.getKeys().remainTimeToLive(key) != -2;
	}
	/**
	 * 删除key
	 * @param key 可以传一个值 或多个
	 */
	public void del(String... key) {
		if (key != null && key.length > 0) {
			redissonClient.getKeys().delete(key);
		}
	}
	// ============================String=============================
	/**
	 * 获取 key 的 value
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key) {
		return redissonClient.getBucket(key).get();
	}
	/**
	 * 永久 set
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public void set(String key, Object value) {
		redissonClient.getBucket(key).set(value);
	}

	/**
	 * 默认set 默认实效时间 DEFAULT_EXPIRE
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public void setDefault(String key, Object value) {
		this.set(key, value, DEFAULT_EXPIRE);
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param seconds 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public void set(String key, Object value, long seconds) {
		if (seconds > 0) {
			redissonClient.getBucket(key).set(value, seconds, TimeUnit.SECONDS);
		} else {
			set(key, value);
		}
	}
	/**
	 * 递增
	 * @param key 键
	 * @param delta 要增加几(大于0)
	 * @return
	 */
	public long incr(String key, long delta) {
		if (delta <= 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redissonClient.getAtomicLong(key).addAndGet(delta);
	}
	/**
	 * 递减
	 * @param key 键
	 * @return 自减 1
	 */
	public long decr(String key) {
		return redissonClient.getAtomicLong(key).decrementAndGet();
	}
}