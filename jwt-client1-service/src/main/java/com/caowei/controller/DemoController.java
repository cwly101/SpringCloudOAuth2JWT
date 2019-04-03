package com.caowei.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@GetMapping("/products")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Map<String, String> getProducts() {
		System.out.println("getProducts...");
		Map<String, String> map = new HashMap<String, String>();
		map.put("苹果", "2.4");
		map.put("香蕉", "1.6");
		map.put("盐", "1.0");
		map.put("usa banana", "3.3");
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			System.out.println(auth.getName());
			for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
				System.out.println(grantedAuthority.getAuthority());
			}
		} else {
			System.out.println("Authentication is null");
		}
		return map;
	}
}
