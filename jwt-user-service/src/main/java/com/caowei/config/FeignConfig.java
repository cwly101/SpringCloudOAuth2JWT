package com.caowei.config;

import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static java.util.concurrent.TimeUnit.SECONDS;
import feign.Retryer;

@Configuration
public class FeignConfig extends FeignClientsConfiguration {

	@Override
	@Bean
	public Retryer feignRetryer() {
		//在1000毫秒内（如果请求失败），每隔100毫秒重试一次，最多重试5次。Default()默认设置的就是这个周期。
		//period（周期，即多久重试一次)，maxAttempts（最大重试几次）
		return new Retryer.Default(100,SECONDS.toMillis(1), 5);  //TimeUnit.SECONDS.toMillis(1)     1秒转换为毫秒数
	}
}
