package com.xunxintech.ruyue.coach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 
  * @Title: RyCoachTicketServiceApplication.java 
  * @Package com.xunxintech.ruyue.coach 
  * @Description  ticket service application
  * @author  谢照锋 xiezf@xunxintech.com
  * @date 2017年1月16日 下午3:25:02 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2016 广州讯心信息科技有限公司.
  *
 */
@RestController
@EnableWebMvc
@EnableGlobalMethodSecurity
@EnableScheduling
@EnableResourceServer
@EnableAutoConfiguration
@ComponentScan({"com.xunxintech.ruyue.coach.*.*","com.xunxintech.ruyue.coach.*"})
@SpringBootApplication(scanBasePackages="com.xunxintech.ruyue.coach")
public class RyCoachTicketServiceApplication {

//	private @Value("${spring.http.encoding.charset:UTF-8}")String charset;
	
	public static void main(String[] args) {
		SpringApplication.run(RyCoachTicketServiceApplication.class, args);
	}
	

	@RequestMapping({"route/{methodProfix}/{method}/{version}"})
	public String route(HttpServletRequest request, HttpServletResponse response, @PathVariable String method, @PathVariable String version, @RequestBody String param,@PathVariable String methodProfix) throws Exception{
		return "";
	}

	@Bean
	public RestTemplate initRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			// Ignore 400
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getRawStatusCode() != 400) {
					super.handleError(response);
				}
			}
		});
		return restTemplate;
	}
	
}
