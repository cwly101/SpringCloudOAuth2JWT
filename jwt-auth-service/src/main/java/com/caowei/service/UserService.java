package com.caowei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.caowei.common.SerializeUtil;
import com.caowei.entity.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class UserService implements UserDetailsService {
	
	Jedis jedis;
	
	@Autowired
	public UserService(JedisPool jedisPool) {
		this.jedis=jedisPool.getResource();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Object object= stringRedisTemplate.opsForHash().get("users:user", username);
		byte[] result= jedis.hget("users:".getBytes(),("users:"+username).getBytes());
		User user=(User) SerializeUtil.unserialize(result);
		if(user==null)
			throw new UsernameNotFoundException("用户："+username+"，没有找到");
		
		return user;
	}

}
