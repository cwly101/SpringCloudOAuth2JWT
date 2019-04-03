package com.caowei.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

/**
 * JWT配置，用于向IOC注入生成的TokenStore
 * @author cwly1
 *
 */
@Configuration
public class JwtConfig {

	@Autowired
	JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Bean
	public TokenStore tokenStore() {
		TokenStore tokenStore=new JwtTokenStore(jwtAccessTokenConverter);
		System.out.println("TokenStore 完成！");
		return tokenStore;
	}
	
	/**
	 * JWT token转换器配置，内部包含采用公钥对JWT信息解密操作。
	 * 皆由框架来完成。开发者无需另行处理。
	 * @return
	 */
	@Bean
	protected JwtAccessTokenConverter jwtTokenEnhancer() {
		JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
		String path="public.txt";
		Resource resource=new ClassPathResource(path);
		try {
			System.out.println(resource.getURI().toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String publicKey;
		try {
			publicKey=new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
		} catch (IOException e) {
			System.out.println("读取公钥异常...");
			throw new RuntimeException(e);
		}
		System.out.println(publicKey);
		converter.setVerifierKey(publicKey);  //公钥
		
		System.out.println("公钥证书读取完成，创建JwtAccessTokenConverter对象，生成JWT转换器");
		return converter;
	}
}
