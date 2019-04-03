package com.caowei.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.caowei.common.SerializeUtil;
import com.caowei.entity.User;
import com.caowei.exception.UserLoginException;
import com.caowei.model.JWT;
import com.caowei.model.UserLoginDTO;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class UserService {

	Jedis jedis;
	@Autowired
	AuthServiceClient client;
	
	@Autowired
	public UserService(JedisPool jedisPool) {
		jedis=jedisPool.getResource();
	}
	
	public UserLoginDTO login(String username,String password) {
		// To do...
		//1. 根据用户名密码查询redis，看用户是否存在，密码是否正确
		byte[] result= jedis.hget("users:".getBytes(),("users:"+username).getBytes());
		User user=(User) SerializeUtil.unserialize(result);		
		if(user==null)
			throw new UserLoginException("用户不存在");
		//说明：使用BCryptPasswordEncoder加密，同一个字符串，两次加密出的结果不一样，所以验证不能使用比对的方式，只能调用其matches方法。
		boolean pwd_valid= new BCryptPasswordEncoder().matches(password, user.getPassword());
		if(!pwd_valid) {
			throw new UserLoginException("密码错误");
		}
		
		//eureka从发现服务，到下发所有可用服务列表给每个服务，及列表的刷新，需要一点时间，通常为30-60秒，所以启动服务后马上调用其它服务
		//可能会未响应，等待60秒即可。
		String type="password";
		//oauth2登录验证，兑换token. 
		//【特别说明】：授权服务器对生成的token不作存储，每一次请求会重新生成。由资源服务器负责存储token，token过期后重新请求获取即可。
		JWT jwt=client.getToken(getAuthorization(), type, username, password);
		if(jwt==null) {
			System.out.println("JWT对象为空");
			throw new UserLoginException("获取token失败");
		}
		
		
		//设置UserLoginDTO对象。
		UserLoginDTO userLoginDTO=new UserLoginDTO();
		userLoginDTO.setJwt(jwt);
		userLoginDTO.setUser(user);
		return userLoginDTO;
	}

	/**
	 * http header中的authorization信息
	 * @return
	 */
	private String getAuthorization() {
		String clientID_secret="user_service:123456";   
		byte[] encode= Base64.encodeBase64(clientID_secret.getBytes());
		String authorization=new String(encode);  //authorization即clientID_secret字符串Base64编码后的值
		//System.out.println(authorization);
		String basic_authorization="Basic "+authorization+" ";	
		return basic_authorization;
	}
	
	/**
	 * 刷新token
	 * @param username
	 * @param refresh_token
	 * @return
	 */
	public UserLoginDTO refreshToken(String refresh_token) {
		String grant_type="refresh_token";
		JWT jwt= client.refreshToken(getAuthorization(), grant_type,refresh_token);
		
		UserLoginDTO userLoginDTO=new UserLoginDTO();
		userLoginDTO.setJwt(jwt);
		//userLoginDTO.setUser(user);
		return userLoginDTO;
	}
}
