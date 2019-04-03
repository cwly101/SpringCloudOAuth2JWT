package com.caowei.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.caowei.model.JWT;
import com.caowei.service.AuthServiceClient;

/**
 * 自定义异常处理器
 * 处理器作用：拦截处理401错误，刷新access_token，之后重新请求。
 * 注：使用zuul过滤器，应该也能实现，但未做测试。
 * @author cwly1
 *
 */
public class MyAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

	private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	AuthServiceClient authServiceClient;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		try {
			// 解析异常，如果是401就处理
			ResponseEntity<?> responseEntity = exceptionTranslator.translate(authException);
			if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {

				String authorization = "Basic dXNlcl9zZXJ2aWNlOjEyMzQ1Ng== ";
				String type = "refresh_token";
				String refresh_token = null;
				Cookie[] cookies = request.getCookies();
				if (request.getCookies() == null) {
					System.out.println("cookie为空，不能刷新token，请重新登录。");
					super.commence(request, response, authException);
					return;
				}
				for (Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "," + cookie.getValue());
					if (cookie.getName().equals("refresh_token"))
						refresh_token = cookie.getValue();
				}
				JWT jwt = authServiceClient.refreshToken(authorization, type, refresh_token);
				if (jwt == null) {   //刷新失败。两种情况：1.token服务器异常  2. refresh_token也过期了。 只能重新登录。
					response.setStatus(401);
					response.setHeader("Content-Type", "application/json;charset=utf-8");
					response.getWriter().print("{\"code\":1,\"message\":\"token过期，访问权限不足！！！\"}");
					response.getWriter().flush();
					//注：前端JS收到该结果，应该跳转到登录页面。
				} else {
					// System.out.println(request.getRequestURI());
					// 响应体中附带返回cookie
					response.addCookie(new Cookie("access_token", jwt.getAccess_token()));
					response.addCookie(new Cookie("refresh_token", jwt.getRefresh_token()));
					response.addCookie(new Cookie("expires_in", String.valueOf(jwt.getExpires_in())));
					System.out.println("jwt access_token:" + jwt.getAccess_token());
					System.out.println("ok!" + request.getHeader("Authorization"));
					// 这种方式开不通，因为header中数据需要更新（过期token替换成最新刷新的token），这种方式无法更新header数据，只能原样传递。
					// request.getRequestDispatcher(request.getRequestURI()).forward(request,
					// response);
					// response.setHeader("Authorization", "bearer "+jwt.getAccess_token());
					// response.sendRedirect("http://localhost:9090"+request.getRequestURI());

					// 这种方法就是为了刷新header中数据。
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "bearer " + jwt.getAccess_token());
					HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
					//System.out.println(request.getMethod());  请求方式，如:GET、POST等
					int port=request.getServerPort();
					String url = "http://localhost:"+port + request.getRequestURI();
					System.out.println(url);
					if (request.getMethod().equals("POST")) {
						System.out.println("send POST...");
						// POST请求方式
						ResponseEntity<String> response2 = restTemplate.postForEntity(
								url, httpEntity, String.class);
						System.out.println(response2.getBody());
						response.getWriter().print(response2.getBody());
					} else if (request.getMethod().equals("GET")) {
						System.out.println("send GET...");
						// GET请求方式					
						RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
						ResponseExtractor<ResponseEntity<String>> responseExtractor = restTemplate
								.responseEntityExtractor(String.class);
						ResponseEntity<String> response3 = restTemplate.execute(url, HttpMethod.GET, requestCallback,
								responseExtractor);
						System.out.println(response3.getBody());
						response.getWriter().print(response3.getBody());
					}
				}
			} else {
				System.out.println(responseEntity.getStatusCode());
				super.commence(request, response, authException);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
