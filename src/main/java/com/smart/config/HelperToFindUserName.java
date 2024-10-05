package com.smart.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class HelperToFindUserName {
	
	public static String getEmailOfLoggedinUser(Authentication authentication) {
		
		if(authentication instanceof OAuth2AuthenticationToken) {
			var authenticationToken=(OAuth2AuthenticationToken) authentication;
			String loginTrough=authenticationToken.getAuthorizedClientRegistrationId();
			var oauthUser=(DefaultOAuth2User) authentication.getPrincipal();
			String email="";
			if(loginTrough.equalsIgnoreCase("google")) {
				email =oauthUser.getAttribute("email").toString();
				System.out.println("using google");
				return email;
				
			}
			else if(loginTrough.equalsIgnoreCase("github")) {
				email=oauthUser.getAttribute("email")!=null?oauthUser.getAttribute("email"):oauthUser.getAttribute("login").toString()+oauthUser.getAttribute("id").toString()+"fake@gmail.com";
				System.out.println("using github");
				return email;
				
			}
			else {
				return "";
			}
		}
		System.out.println("using normal ways");
		return authentication.getName();
		
	}
	public static String getProvider(Authentication authentication) {
		if(authentication instanceof OAuth2AuthenticationToken) {
			var authenticationToken=(OAuth2AuthenticationToken) authentication;
			String loginTrough=authenticationToken.getAuthorizedClientRegistrationId();
			return loginTrough;
		}
		return null;
	}
}
