package com.caowei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

	@Bean
	public Docket createRestApi() {
		Docket docket=new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.caowei.controller"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}
	
	private ApiInfo apiInfo() {
		ApiInfo apiInfo=new ApiInfoBuilder()
				.title("OAuth2资源服务器API")
				.description("OAuth2资源服务器提供的API接口文档")
				.termsOfServiceUrl("http://localhost:9090/swaggerui.html")
				.version("0.9")
				.build();
		return apiInfo;
	}
}
