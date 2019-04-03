package com.caowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JwtEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtEurekaServerApplication.class, args);
		System.out.println("jwt eureka server 服务发现与注册服务器启动完成！");
	}

}
