package com.smart.config;

import java.awt.RenderingHints.Key;
import java.io.IOException;

import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	Logger logger= LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
	@Autowired
	private UserRepository userRepository;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		var oauthAuthenticationToken=(OAuth2AuthenticationToken) authentication;
		String loginTrough=oauthAuthenticationToken.getAuthorizedClientRegistrationId();
		var oauthUser=(DefaultOAuth2User) authentication.getPrincipal();
		oauthUser.getAttributes().forEach((key,value)->{
			logger.info(key +" : "+value);
		});
		
		User user=new User();
		user.setEnabled(true);
		user.setRole("ROLE_NORMAL");
		user.setPassword("default password");
		if(loginTrough.equalsIgnoreCase("google")) {
			user.setEmail(oauthUser.getAttribute("email").toString());
			user.setName(oauthUser.getAttribute("name").toString());
			user.setImageUrl(oauthUser.getAttribute("picture").toString());
		}
		else if(loginTrough.equalsIgnoreCase("github")){
			String email=oauthUser.getAttribute("email")!=null?oauthUser.getAttribute("email"):oauthUser.getAttribute("login").toString()+oauthUser.getAttribute("id").toString()+"fake@gmail.com";
			user.setEmail(email);
			user.setName(oauthUser.getAttribute("login"));
			user.setImageUrl(oauthUser.getAttribute("avatar_url"));
			System.out.println("generated email id:"+user.getEmail());
			
		}
		else {
			//may be from facebook
		}
		
		
		/*
		DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
		user.getAttributes().forEach((key,value)->{
			logger.info("{}=>{}",key,value);
		});
		User newUser=new User();
		newUser.setEmail(user.getAttribute("email").toString());
		newUser.setName(user.getAttribute("name").toString());
		newUser.setImageUrl(user.getAttribute("picture").toString());
		newUser.setRole("ROLE_NORMAL");
		newUser.setPassword("hello");//default password
		newUser.setEnabled(true);
		if(userRepository.getUserByEmail(user.getAttribute("email")) == null) {
			userRepository.save(newUser);
			logger.info("user saved new");
			}
		
		*/
		if(userRepository.getUserByEmail(user.getEmail()) == null) {
			System.out.println(user.getId());
			userRepository.save(user);
			logger.info("user saved new");
			}
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/index");
		
	}
	

}
