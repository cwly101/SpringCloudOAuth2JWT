package com.caowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class JwtZuulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtZuulApiApplication.class, args);
		System.out.println("Zuul API 启动完成！");
	}

}
