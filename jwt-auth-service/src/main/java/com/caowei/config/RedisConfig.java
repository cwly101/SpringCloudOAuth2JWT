package com.caowei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	@Autowired
	RedisProperties properties;
	
	@Bean
	public JedisPool redisPoolFactory() {
		int max_idle=properties.getJedis().getPool().getMaxIdle();
		int max_active=properties.getJedis().getPool().getMaxActive();
		long max_wait_millis=properties.getJedis().getPool().getMaxWait().toMillis();
		String host=properties.getHost();
		//System.out.println("max_idle:"+max_idle+",max_active:"+max_active+",wait_millis:"+max_wait_millis);
		
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMaxIdle(max_idle);
		config.setMaxTotal(max_active);
		config.setMaxWaitMillis(max_wait_millis);
		JedisPool pool=new JedisPool(config, host,properties.getPort(),100);
		System.out.println("RedisConfig加载完成！");
		return pool;
	}
}
