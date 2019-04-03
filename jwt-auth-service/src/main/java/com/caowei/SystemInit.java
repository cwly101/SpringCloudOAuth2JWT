package com.caowei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.caowei.common.SerializeUtil;
import com.caowei.entity.Role;
import com.caowei.entity.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class SystemInit implements ApplicationRunner {
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("系统初始化开始...");
		init();
		System.out.println("系统初始化完成！");
	}
	
	/**
	 * 系统初始化函数
	 */
	private void init() {
		usersToRedis();
	}

	/**
	 * 仅能操作字符串类型。如果一个java bean中包含boolean或其它非字符串类型，将导致失败。
	 */
//	@Autowired
//	StringRedisTemplate stringRedisTemplate;  
	
	
	Jedis jedis;
	
	@Autowired
	public SystemInit(JedisPool jedisPool) {
		this.jedis=jedisPool.getResource();
	}
	
	/**
	 * 初始化用户信息到Redis中
	 */
	private void usersToRedis() {		
		String key="users:";
		if(jedis.exists(key)) {
			System.out.println("用户列表已存在于redis中，无需重复初始化");
			return;
		}	
		
		
		//要添加到redis中的集合
		//Map<String, String> m=new HashMap<String, String>();
		//管理员角色
		Role role_admin=new Role();
		role_admin.setId(1);
		role_admin.setRolename("ROLE_ADMIN");
		//普通用户角色
		Role role_user=new Role();
		role_user.setId(2);
		role_user.setRolename("ROLE_USER");
		
		User u1=new User();
		u1.setUsername("cw");
		u1.setPassword(new BCryptPasswordEncoder().encode("123456"));
		List<Role> roles=new ArrayList<Role>();
		roles.add(role_admin);
		roles.add(role_user);
		u1.setAuthorities(roles);
		
		
		User u2=new User();
		u2.setUsername("tester");
		u2.setPassword(new BCryptPasswordEncoder().encode("test"));
		List<Role> roles2=new ArrayList<Role>();
		roles2.add(role_user);
		u2.setAuthorities(roles2);
		//参见：https://blog.csdn.net/uncletian/article/details/80067084
		Map<byte[], byte[]> map_user=new HashMap<byte[], byte[]>();
		map_user.put(("users:"+u1.getUsername()).getBytes(), SerializeUtil.serialize(u1));
		map_user.put(("users:"+u2.getUsername()).getBytes(), SerializeUtil.serialize(u2));
		System.out.println(map_user.size());
		jedis.hmset("users:".getBytes(), map_user);  //一次批量存储
		//jedis.hset("users:".getBytes(), ("users:"+u2.getUsername()).getBytes(), SerializeUtil.serialize(u2));  //单条存储
		System.out.println("用户信息到Redis完成！");
	}
}
