package com.caowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthServiceApplication.class, args);
		System.out.println("jwt-auth-service (uaa)授权服务启动完成！");
		//仅作为oauth2授权服务器，不提供除获取token以外的任务资源服务。
	}

}
