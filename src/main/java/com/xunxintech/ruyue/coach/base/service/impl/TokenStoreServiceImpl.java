package com.xunxintech.ruyue.coach.base.service.impl;

import com.xunxintech.ruyue.coach.base.service.TokenStoreService;
import com.xunxintech.ruyue.coach.utils.JSON;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenStoreServiceImpl implements TokenStoreService
{
	private @Autowired RemoteTokenServices remoteTokenServices;
	
	private @Value("${security.oauth2.resource.client-id}") String clientId;
	private @Value("${security.oauth2.resource.client-secret}") String clientSecret;
	private @Value("${security.oauth2.resource.tokenInfoUri}") String tokenInfoUri;

	private@Autowired RestOperations restTemplate;
	
	private String tokenName = "token";

	private static Logger logger = LoggerFactory.getLogger(TokenStoreServiceImpl.class);
	
	@Override
//	@Cacheable("tokenStoreKey")
//	@SuppressWarnings({"unchecked", "rawtypes"})
	public Map<String, Object> postForMap(String token)
	{
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add(tokenName, token);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		try
		{
			String asString = Request.Get(tokenInfoUri + "?token=" + token).execute().returnContent().asString();

			//TODO BUG
			logger.error("######################################################################################");
			logger.error("######################################################################################");
			logger.error(asString);

			Map map = restTemplate.exchange(tokenInfoUri, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, headers),
					new HashMap<String, Object>().getClass()).getBody();
			logger.error(JSON.objectMapper.writeValueAsString(map));
			logger.error("######################################################################################");
			logger.error("######################################################################################");
			logger.error("######################################################################################");
			
			
//			return JSON.objectMapper.readValue(asString, JSON.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
			return map;
		}
		catch (HttpMessageNotReadableException e)
		{
			throw new InvalidTokenException(token);
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InvalidTokenException(token);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InvalidTokenException(token);
		}
		
	}

	private String getAuthorizationHeader(String clientId, String clientSecret) {

		if(clientId == null || clientSecret == null) {
			logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
		}

		String creds = String.format("%s:%s", clientId, clientSecret);
		try {
			return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String");
		}
	}

	
}

