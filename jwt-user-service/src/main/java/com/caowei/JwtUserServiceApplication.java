package com.caowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OAuth2资源服务器，同时也是网关服务器。第三方应用调用各个微服务模块功能，只能通过网关这一途径。
 * 网关内部调用各个微服务模块api。 所有微服务模块只对网关开放，不对外开放。
 * @author cwly1
 *
 */
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients   //spring-cloud-starter-openfeign，默认添加并启用了Ribbon（负载均衡）、Hystrix（熔断器）功能。
//feign发送http请求支持3种http客户端：httpURLConnection、OKHttp、httpClient.
//默认使用httpURLConnection，如果想替换，在pom中直接引入想替换http客户端即可。（不要全引入，否则feign会不知所措）
public class JwtUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtUserServiceApplication.class, args);
		System.out.println("jwt user service 资源服务器（兼具Zuul网关服务器）开启！");
		//仅作为提供用户信息的oauth2资源服务器，不提供其它任何服务。
		//注：可以将oauth2授权服务器与资源服务器合并成一个，这里使用二者分离的方式。
		
		/**
		 * 利弊：
		 * 1. 第三方应用自行存储token，这里不需要请求每个需要验证权限的资源都与oauth2服务器交互，减轻服务器压力。
		 * 2. 第三方应用不存储token，每次都与oauth2服务器交互，这样不占用第三方任何内存资源。 但实际应用中，这种方案几乎不采用。
		 * 注：一般来说，oauth2服务器与第三方应用，都会各自保存一份token。
		 * 如果同属一个公司旗下的多个产品，即oauth2服务器与多个调用它的应用同属一家公司，就另当另论了。
		 */
	}

}
