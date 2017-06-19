package com.xunxintech.ruyue.coach.base.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Primary
@Qualifier
public class CoachRemoteTokenServices extends RemoteTokenServices
{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
	
	private @Autowired TokenStoreService tokenStoreService;

    @Override
	public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
		Map<String, Object> map = tokenStoreService.postForMap(accessToken);
		if (map.containsKey("error")) {
			logger.debug("check_token returned error: " + map.get("error"));
			throw new InvalidTokenException(accessToken);
		}

		Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server");
		OAuth2Authentication auth = tokenConverter.extractAuthentication(map);
		auth.setAuthenticated(true);
		return auth;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

}
