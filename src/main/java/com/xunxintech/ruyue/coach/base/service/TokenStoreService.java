package com.xunxintech.ruyue.coach.base.service;

import java.util.Map;

/**
 * 
  * @Title: TokenStoreService.java 
  * @Package com.example.base.service 
  * @Description  TODO
  * @author  XZF
  * @date 2017年5月9日 下午3:58:24 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2017 广州讯心信息科技有限公司.
  *
 */
public interface TokenStoreService
{
	Map<String, Object> postForMap(String token);
}
