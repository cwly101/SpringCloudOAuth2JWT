package com.caowei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * OAuth2资源服务器
 * 资源服务器，需要配置HttpSecurity和ResourceServerSecurityConfigurer
 * @author cwly1
 *
 */
@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	TokenStore tokenStore;
	
	/**
	 * HttpSecurity 配置哪些请求需要验证，哪些不需要。
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		 .csrf().disable()
		 .authorizeRequests()
		 //"/user/register","/user/refreshtoken"
		 .antMatchers("/user/login","/user/register","/user/refreshtoken","/swagger-ui.html","/webjars/**","/v2/**","/swagger-resources/**")
		 .permitAll()  //两个路径允许访问
		 .antMatchers("/**").authenticated() //其它皆需验证
		 ;
	}
	
	
	
	/**
	 * ResourceServerSecurityConfigurer配置tokenStore
	 * 注：token信息的存储在此配置好之后，由框架接手管理，包括token的保存及对请求携带token的验证工作，皆由框架完成。
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources
		   .tokenStore(tokenStore)
		   .authenticationEntryPoint(new MyAuthenticationEntryPoint())   //将处理器设置到过滤器上
		   ;
	}
}
