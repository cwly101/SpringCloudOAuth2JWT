package com.caowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 每一个需要验证权限才能访问的独立微服务模块，都是一个oauth2资源服务器
 * 这里对采用JWT非对称加密方式传递来的JWT信息进行校验。校验需要使用公钥，即资源文件夹下的public.txt公钥
 * @author cwly1
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class JwtClient1ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtClient1ServiceApplication.class, args);
		System.out.println("JwtClient1Service 启动！");
		
	}

}
