package com.xunxintech.ruyue.coach.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

/**
 * 
  * @Title: ErrorMessageService.java 
  * @Package com.xunxintech.ruyue.coach.base 
  * @Description  error message
  * @author  XZF
  * @date 2017年6月2日 下午2:45:25 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2017 广州讯心信息科技有限公司.
  *
 */
public class ErrorMessageService
{
	private static Logger logger  = LoggerFactory.getLogger(ErrorMessageService.class);
	public static String getResponseMessage(Throwable error){
		if (error instanceof InvalidTokenException)
		{
			return "token invalid";
		}
		//TODO 在这里添加错误类型
		
		
		
		if(error != null){
			logger.error("ticke,no process exception, className:{}",error.getClass().getName());
		}
		return null;
	}
}
