package com.caowei.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caowei.model.UserLoginDTO;
import com.caowei.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class UserController {

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@ApiOperation(value="/user/register",notes="新用户注册（暂不可用，不开放）")
	@GetMapping("/user/register")
	public String userRegister(
			@RequestParam("username") @ApiParam(value="用户名")String username,
			@RequestParam("password") @ApiParam(value="密码")String password
			) {
		//测试url:  
		//http://localhost:9090/user/register?username=cw&password=123root
		
		String json="{\"state\":\"error\",\"msg\":\"暂不开放注册\"}";
		return json;
	}
	
	@Autowired
	UserService userService;
	
	@ApiOperation(value="/user/login",notes="用户登录获取token")
	@GetMapping("/user/login")
	public UserLoginDTO login(
			@RequestParam("username") @ApiParam(value="用户名")String username,
			@RequestParam("password") @ApiParam(value="密码")String password,
			HttpServletResponse response
			) {
		System.out.println("UserLoginDTO login 执行，"+username+","+password);
		UserLoginDTO userLoginDTO=userService.login(username, password);
		System.out.println("access_token："+userLoginDTO.getJwt().getAccess_token());
		
		//响应体中附带返回cookie
		response.addCookie(new Cookie("access_token", userLoginDTO.getJwt().getAccess_token()));
		response.addCookie(new Cookie("refresh_token", userLoginDTO.getJwt().getRefresh_token()));
		response.addCookie(new Cookie("expires_in", String.valueOf(userLoginDTO.getJwt().getExpires_in())));
		return userLoginDTO;
	}
	
	@ApiOperation(value="/user/foo",notes="测试方法，登录成功，并具有ROLE_USER权限的用户才能访问。请求头中需要附带token")
	@GetMapping("/user/foo")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String foo(
			@RequestHeader(value="Authorization") @ApiParam(value="token_type access_token")String authorization
			) {
		//获取当前用户信息
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getName());
//		for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
//			System.out.println(grantedAuthority.getAuthority());
//		}
		return "hello,"+auth.getName();
	}
	
	@ApiOperation(value = "/user/refreshtoken",notes = "刷新token测试方法")
	@GetMapping("/user/refreshtoken")
	public UserLoginDTO refreshToken(
			//@RequestParam("username") String username,
			@RequestParam("refreshtoken") @ApiParam(value="refresh_token")String refresh_token
			) {
		//System.out.println(username+","+refresh_token);
		return userService.refreshToken( refresh_token);
	}
}
