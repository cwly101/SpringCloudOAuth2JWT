package com.caowei.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//说明：要获取yml文件的服务器端口，必须指定webEnvironment，否则获取的值固定为-1。这只是单位测试中的方式。
//参见：https://stackoverflow.com/questions/46684818/springboot-test-valueserver-port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserServiceTest {

	@Autowired
	UserDetailsService userService;
	
	//@Test
	public void loadUserByUsernameTest() {
		UserDetails ud= userService.loadUserByUsername("cw");
		System.out.println(ud.getUsername());
		System.out.println("=========");
		for (GrantedAuthority role : ud.getAuthorities()) {
			System.out.println(role.getAuthority());
		} 
	}
	
	@Value("${server.port}")
	String server_port;
	
	@Test
	public void fileIsExistTest() throws IOException {
		System.out.println("====================");
		System.out.println(server_port);
		System.out.println("fzp-jwt.jks文件是否存在：");
		String path="fzp-jwt.jks";
		//spring boot获取resource目录下文件。 参见：https://blog.csdn.net/napoay/article/details/81048724
		Resource resource = new ClassPathResource(path);
        File file = resource.getFile();
        System.out.println(file.exists());
        System.out.println(file.getPath());
	}
}
