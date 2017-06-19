package com.xunxintech.ruyue.coach.base.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunxintech.ruyue.coach.base.BaseResponse;
import com.xunxintech.ruyue.coach.base.ErrorMessageService;
import com.xunxintech.ruyue.coach.utils.JSON;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@Qualifier
@RequestMapping(value = "${server.error.path:${error.path:/error}}",produces = "text/html; charset=UTF-8")
@EnableConfigurationProperties({ServerProperties.class})
public class BaseErrorController implements ErrorController
{
	
//	private static final String MESSAGE = "发生一个预期之外的错误";

	private static Logger logger = LoggerFactory.getLogger(BaseErrorController.class);

	private ErrorAttributes errorAttributes;
	@Autowired
	private ServerProperties serverProperties;

	/**
	 * 初始化ExceptionController
	 * @param errorAttributes
	 */
	@Autowired
	public BaseErrorController(ErrorAttributes errorAttributes)
	{
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

	/**
	 * 定义500的ModelAndView
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response)
	{
		response.setStatus(getStatus(request).value());
		Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
		return new ModelAndView("error", model);
	}

	/**
	 * 定义500的错误JSON信息
	 * @param request
	 * @return
	 * @throws JsonProcessingException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String error(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException
	{
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		
		Map<String, Object> body = getErrorAttributes(requestAttributes, isIncludeStackTrace(request, MediaType.ALL));
		
		logger.error("order service system exception, message json: {}", JSON.objectMapper.writeValueAsString(body));
		
		String responseMessage = getResponseMessage(requestAttributes);
		
		HttpStatus status = getStatus(request);
		request.setCharacterEncoding(String.valueOf(Consts.UTF_8));
		
		BaseResponse<String> result = new BaseResponse<String>(String.valueOf(status.value()), responseMessage != null?responseMessage:String.valueOf(body.get("message")));
		
		return JSON.objectMapper.writeValueAsString(result);
	}

	public String getResponseMessage(RequestAttributes requestAttributes){
		Throwable error = errorAttributes.getError(requestAttributes);
		return ErrorMessageService.getResponseMessage(error);
	}
	/**
	 * Determine if the stacktrace attribute should be included.
	 * @param request the source request
	 * @param produces the media type produced (or {@code MediaType.ALL})
	 * @return if the stacktrace attribute should be included
	 */
	protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces)
	{
		ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
		if (include == ErrorProperties.IncludeStacktrace.ALWAYS) { return true; }
		if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) { return getTraceParameter(request); }
		return false;
	}

	/**
	 * 获取错误的信息
	 * @param request
	 * @param includeStackTrace
	 * @return
	 */
	private Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace)
	{
		return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}
	/**
	 * 
	 * @Title getErrorAttributes
	 * @Description 获取误信息
	 * @param request
	 * @param includeStackTrace
	 * @return Map<String,Object>   
	 * @throws
	 */
	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace)
	{
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return getErrorAttributes(requestAttributes, includeStackTrace);
	}
	/**
	 * 是否包含trace
	 * @param request
	 * @return
	 */
	private boolean getTraceParameter(HttpServletRequest request)
	{
		String parameter = request.getParameter("trace");
		if (parameter == null) { return false; }
		return !"false".equals(parameter.toLowerCase());
	}

	/**
	 * 获取错误编码
	 * @param request
	 * @return
	 */
	private HttpStatus getStatus(HttpServletRequest request)
	{
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) { return HttpStatus.INTERNAL_SERVER_ERROR; }
		try
		{
			return HttpStatus.valueOf(statusCode);
		}
		catch (Exception ex)
		{
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	/**
	 * 实现错误路径,暂时无用
	 * @see ExceptionMvcAutoConfiguration#containerCustomizer()
	 * @return
	 */
	@Override
	public String getErrorPath()
	{
		return "";
	}
	
}
