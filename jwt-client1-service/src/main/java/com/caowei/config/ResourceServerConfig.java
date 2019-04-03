package com.caowei.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

/**
 * 资源服务器配置
 * @author cwly1
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Bean
	public TokenStore tokenStore() {
		TokenStore tokenStore=new JwtTokenStore(jwtAccessTokenConverter());
		return tokenStore;
	}
	
	/**
	 * 配置JWT使用的解密密钥。这里采用非对称加密方式，即RS256。
	 * JWT：{"alg":"RS256","typ":"JWT"}
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
		converter.setVerifierKey(getPubKey());
		return converter;
	}
	
	/**
	 * 获取非对称加密的公钥。用于对请求发送来的JWT解密。
	 * @return
	 */
	private String getPubKey() {
		Resource resource=new ClassPathResource("public.txt");
		String publicKey;
		try {
			publicKey=new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
			System.out.println("公钥key：/n"+publicKey);
			return publicKey;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
}
