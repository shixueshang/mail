package com.coolgua.mail.service.impl;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.coolgua.common.service.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

	private Logger log = Logger.getLogger(RedisServiceImpl.class);
	
	@Resource
	private JedisPool jedisPool;

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}

	/**
	 * 关闭资源
	 * 
	 * @param jedis
	 */
	private void closeJedis(Jedis jedis) {
		try {
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}
	
	public String set(String key, String value, int second) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String set = jedis.set(key, value);
			jedis.expire(key, second);
			return set;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}

	public String hget(String hkey, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hget(hkey, key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return null;
	}

	public long hset(String hkey, String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hset(hkey, key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}

	public long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.incr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}

	public long expire(String key, int second) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.expire(key, second);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}

	public long ttl(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.ttl(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}

	public long del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}

	public long hdel(String hkey, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hdel(hkey, key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			closeJedis(jedis);
		}
		return 0;
	}
}
