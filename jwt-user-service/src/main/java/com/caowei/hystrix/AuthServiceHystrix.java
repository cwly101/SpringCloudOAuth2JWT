package com.caowei.hystrix;

import org.springframework.stereotype.Component;

import com.caowei.model.JWT;
import com.caowei.service.AuthServiceClient;

@Component
public class AuthServiceHystrix implements AuthServiceClient {

	@Override
	public JWT getToken(String authorization, String type, String username, String password) {
		System.out.println("远程服务未响应，AuthServiceHystrix熔断器getToken执行，固定返回null");
		return null;
	}

	@Override
	public JWT refreshToken(String authorization, String type, String refresh_token) {
		System.out.println("刷新token，远程服务未响应，固定返回null\n"+authorization);
		return null;
	}

}
