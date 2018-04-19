package com.coolgua.mail.config;

import com.coolgua.mail.interceptor.AntiInjectInterceptor;
import com.coolgua.mail.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.coolgua.mail.controller", "com.coolgua.common.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public AntiInjectInterceptor antiInjectInterceptor() {
		return new AntiInjectInterceptor();
	}

	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}

	/**
	 * 拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 防注入拦截器
		registry.addInterceptor(antiInjectInterceptor()).addPathPatterns("/**");

		registry.addInterceptor(loginInterceptor()).addPathPatterns("/interface/**");

		super.addInterceptors(registry);
	}

}
