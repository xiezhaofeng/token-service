package com.xunxintech.ruyue.coach.base.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 
  * @Title: WebMvcConfig.java 
  * @Package com.xunxintech.ruyue.coach.config 
  * @Description  TODO
  * @author  XZF
  * @date 2017年1月16日 下午3:25:32 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2016 广州讯心信息科技有限公司.
  *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { 
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
   
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//    	registry.addInterceptor(configFilter).addPathPatterns("/v1/**");
        super.addInterceptors(registry);
    }
 
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		super.configureMessageConverters(converters);
		converters.add(responseBodyConverter());
	}
	
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
	    StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
	    return converter;
	}
}
