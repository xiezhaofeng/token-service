package com.xunxintech.ruyue.coach.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.xunxintech.ruyue.coach.base.service.CoachRemoteTokenServices;

@Configuration
@Qualifier
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	private @Autowired CoachRemoteTokenServices tokenService;
	

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
	    authenticationManager.setTokenServices(tokenService);
	    return authenticationManager;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
//		.requestMatchers().antMatchers("/route/tik/**").and()
		.authorizeRequests()
			.antMatchers("/route/tik/**")
				.hasAuthority("CORE_TIK")
			.antMatchers("/route/bsc/**")
				.hasAnyAuthority("CORE_BSC")//, "CORE_TIK"
		.anyRequest().authenticated()
		.and()
			.anonymous()
				.authenticationFilter(new OAuthAnonymousFilter(String.valueOf(System.currentTimeMillis())));
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
	
}
