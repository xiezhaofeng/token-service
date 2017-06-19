package com.xunxintech.ruyue.coach.base.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunxintech.ruyue.coach.base.BaseResponse;
import com.xunxintech.ruyue.coach.utils.JSON;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * 
 * @Title: BaseExceptionHandler.java
 * @Package com.xunxintech.ruyue.coach.config
 * @Description TODO
 * @author 谢照锋 xiezf@xunxintech.com
 * @date 2017年1月16日 下午3:25:38
 * @version
 *
 * @Copyrigth 版权所有 (C) 2016 广州讯心信息科技有限公司.
 *
 */
@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler
{

	private static Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

	@ExceptionHandler({ SQLException.class, NullPointerException.class, ParseException.class })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws JsonProcessingException
	{
		handleLog(request, ex);
		return showJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), "service exception!!" + ex.getMessage() + "  ," + HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
	}

//	@ExceptionHandler(ParameterException.class)
//	@ResponseStatus(value = HttpStatus.OK)
//	@ResponseBody
//	public String handleParameterException(HttpServletRequest request, ParameterException ex) throws JsonProcessingException
//	{
//		handleLog(request, ex);
//		return showJson(1001, "parmaneter ecception", request);
//	}



	/**
	 * @throws JsonProcessingException
	 * 
	 * @Title handleSystemException
	 * @Description 系统错误
	 * @param request
	 * @param ex
	 * @return
	 * @throws JsonProcessingException String
	 * @throws
	 */
	@ExceptionHandler(SystemException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String handleSystemException(HttpServletRequest request, SystemException ex) throws JsonProcessingException
	{
		handleLog(request, ex);
		return showJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), "", request);
	}

	/**
	 * @throws JsonProcessingException
	 * 
	 * @Title handleAllException
	 * @Description json转换错误
	 * @param request
	 * @param ex
	 * @return String
	 * @throws
	 */
	@ExceptionHandler(JsonProcessingException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String handleAllException(HttpServletRequest request, JsonProcessingException ex) throws JsonProcessingException
	{
		handleLog(request, ex);
		return showJson(500, "service jsonException", request);

	}

	
	private void handleLog(HttpServletRequest request, Exception ex)
	{
		StringBuffer logBuffer = new StringBuffer();
		if (request != null)
		{
			logBuffer.append("  request method=" + request.getMethod());
			logBuffer.append("  url=" + request.getRequestURL());
		}
		if (ex != null)
		{
			logBuffer.append("  exception:" + ex);
		}
		logger.error(logBuffer.toString());

		if (logger.isDebugEnabled())
		{
			ex.printStackTrace();
		}
	}

	private String showJson(Integer code, String msg, HttpServletRequest request) throws JsonProcessingException
	{

		BaseResponse<String> baseResponse = new BaseResponse<String>(String.valueOf(code), msg);
		return JSON.objectMapper.writeValueAsString(baseResponse);
	}
}
