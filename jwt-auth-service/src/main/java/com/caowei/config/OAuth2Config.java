package com.caowei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * OAuth2授权服务器
 * 
 * @author cwly1
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

	// 作为授权服务器需要配置两个选项，即ClientDetailsServiceConfigurer和AuthorizationServerEndpointsConfigurer

	/**
	 * ClientDetailsServiceConfigurer 配置客户端基本信息。
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() // 信息存储在内存中
				.withClient("user_service") // 创建一个客户端。作为授权服务器，初始化时至少配置一个客户端。
				.secret(new BCryptPasswordEncoder().encode("123456")).scopes("service")
				.authorizedGrantTypes("refresh_token", "password").accessTokenValiditySeconds(3600) // token过期时间为3600秒
		;
		System.out.println("OAuth2授权服务器【客户端基本信息】配置完成！");
	}

	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * AuthorizationServerEndpointsConfigurer 配置token存储方式和 authenticationManager.
	 * 注：authenticationManager需要配置AuthenticationManager这个bean，这个bean来源于WebSecurityConfigurerAdapter的配置，
	 * 只有配置了这个bean才会开启密码类型的验证。
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// tokenStore使用了JwtTokenStore
		endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer())
				.authenticationManager(authenticationManager)
				// 没有它，在使用refresh_token的时候会报错 IllegalStateException, UserDetailsService is
				// required.
				.userDetailsService(userDetailsService)
				// 不加报错"method_not_allowed",
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.GET);
		System.out.println("OAuth2授权服务器【token存储方式和开启密码验证】配置完成！");
	}

	/**
	 * 开启密码类型验证。
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public TokenStore tokenStore() {
		// JwtTokenStore并没有做任何存储，tokenStore需要一个JwtTokenConverter对象，用于Token转换。
		JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer());
		return jwtTokenStore;
	}

	/**
	 * 获取JwtTokenConverter对象
	 * 
	 * @return
	 */
	@Bean
	protected JwtAccessTokenConverter jwtTokenEnhancer() {
		String path = "fzp-jwt.jks";
		String password = "fzp123";
		String alias = "fzp-jwt";
		KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(new ClassPathResource(path), password.toCharArray());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyFactory.getKeyPair(alias));
		return converter;
	}

}
