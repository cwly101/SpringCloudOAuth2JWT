package com.caowei.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.caowei.config.FeignConfig;
import com.caowei.hystrix.AuthServiceHystrix;
import com.caowei.model.JWT;

@FeignClient(
		value="uaa-auth-service",  //调用的远程服务的名称
		configuration=FeignConfig.class,  //feign配置类，指请求失败情况下，重试次数的配置。
		fallback=AuthServiceHystrix.class,  //熔断器的处理类
		decode404=false  //false是默认值。指如果返回404错误，是解码还是抛异常。false指不解码，抛异常。
		)
public interface AuthServiceClient {

	/**
	 * 获取token
	 * @param authorization
	 * @param type
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping(value="/oauth/token")
	JWT getToken(
			@RequestHeader(value="Authorization") String authorization,
			@RequestParam("grant_type") String type,
			@RequestParam("username") String username,
			@RequestParam("password") String password);

	/**
	 * 刷新token
	 * @param authorization
	 * @param type
	 * @param refresh_token
	 * @return
	 */
	@PostMapping(value="/oauth/token")
	JWT refreshToken(
			@RequestHeader(value="Authorization") String authorization,
			@RequestParam("grant_type") String type,
			@RequestParam("refresh_token") String refresh_token
			);
	
}
