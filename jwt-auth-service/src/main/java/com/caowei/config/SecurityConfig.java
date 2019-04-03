package com.caowei.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 开启OAuth2的密码验证策略方式。
	 * 注：只有注入AuthenticationManager才能开启密码验证策略。
	 */
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		 .csrf().disable()
		 .exceptionHandling()
		 //用来解决匿名用户访问无权限资源时的异常。AuthenticationEntryPoint接口的匿名实现类。 即，默认情况下登陆失败会跳转页面
		 //参见：https://blog.csdn.net/jkjkjkll/article/details/79975975
		 .authenticationEntryPoint((request,response,authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
		 .and()
		 .authorizeRequests()
		 .anyRequest().authenticated()
		 .and()
		 .httpBasic()
		 ;
		System.out.println("SecurityConfig configure(HttpSecurity http)验证策略配置完成！");
	}
	
	@Autowired
	UserDetailsService userDetailsService; 
	
	/**
	 * 配置验证的用户信息源、密码加密策略。
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//super.configure(auth);
		auth.userDetailsService(userDetailsService)
		 .passwordEncoder(new BCryptPasswordEncoder())
		 ;
		System.out.println("SecurityConfig  configure(AuthenticationManagerBuilder auth)配置验证的用户信息源、密码加密策略配置完成！");
	}
	
	/**
	 * 作用：对用户授权时输入的登录密码采用指定方式进行加解密。
	 *       （这个配置必须有，否则抛：There is no PasswordEncoder mapped for the id "null" 异常）
	 * 说明：SpringBoot2.0抛弃了原来的NoOpPasswordEncoder，要求用户保存的密码必须要使用加密算法后存储，
	 * 在登录验证的时候Security会将获得的密码在进行编码后再和数据库中加密后的密码进行对比
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
	    // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	    return new BCryptPasswordEncoder();
	    
	    //注：PasswordEncoderFactories.createDelegatingPasswordEncoder()方法默认返回值为BCryptPasswordEncoder()，两个return等价
	}
}
