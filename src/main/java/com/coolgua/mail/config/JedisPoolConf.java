package com.coolgua.mail.config;

import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * Redis 装配
 *  
 * @author ethan
 *
 */
@Configuration
@ConditionalOnClass({JedisPool.class})
public class JedisPoolConf {

	@Resource
	private Environment env;

    @Bean
    public JedisPool getJedisPool() {
    	JedisPoolConfig config = new JedisPoolConfig();
    	config.setMaxIdle(Integer.parseInt(env.getProperty("spring.redis.pool.max-idle")));
    	config.setMinIdle(Integer.parseInt(env.getProperty("spring.redis.pool.min-idle")));
    	config.setMaxWaitMillis(Integer.parseInt(env.getProperty("spring.redis.pool.max-wait")));
    	config.setTestOnBorrow(false);
    	return new JedisPool(config, env.getProperty("spring.redis.host"), Integer.valueOf(env.getProperty("spring.redis.port")), Integer.valueOf(env.getProperty("spring.redis.timeout")), env.getProperty("spring.redis.password"));
    }
}
