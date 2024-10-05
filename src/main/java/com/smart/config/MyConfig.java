package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.AbstractLdapAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/*
 * to implement spring security for user login or admin login  we have to follow three step 
 * 1. first we have to make CustomUserDetails which is implement UserDetails interface and override its unimplemented method
 * 2. second we have to make UserDetailServiceImp which is implement of UserDetailServices interface and override its unimplemented
     method where we use CustomUserDetails
 * 3. third we have configure where we HAVE created three beans first for UserDetailServices from where user detail is fetched and second 
 	 BCryptPasswordEncoder and third for SecurityFilterChain where we specify which type user cna access which url
 */

@Configuration
@EnableMethodSecurity
public class MyConfig{
	/*
	 * To learn latest Spring security see Spring_Security project
	 */
	@Autowired
	private OAuthAuthenticationSuccessHandler handler;
	
	@Bean  //this bean is used to Authentication which is done in CustomUserDetails
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	@Bean  //this bean is used in password encoding in HomeContorller
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//		
//		httpSecurity.authorizeRequests()
//		.requestMatchers("/user/**").hasRole("NORMAL")
//		.requestMatchers("/Admin/**").hasRole("ADMIN")
//		.requestMatchers("/**").permitAll()
//		.anyRequest().authenticated()   // not required bcs i had already declare everything in above three line
//		.and().formLogin().loginPage("/signin").and().csrf().disable();
//		return httpSecurity.build();
		/*
		 * if we dont want to above things then we can do it using annotation on handler(@PreAuthorize("hasRole('NORMAL')")  : see user controller)
		  but before that we have to enable it using @EnableMethodSecurity in this config class 
		 */

		httpSecurity.authorizeRequests()
		.requestMatchers("/user/**").authenticated()
//		.requestMatchers("/Admin/**").hasRole("ADMIN")
		.requestMatchers("/**").permitAll()
		.anyRequest().authenticated()   // not required bcs i had already declare everything in above three line
		.and().formLogin()
		.loginPage("/signin")
		.loginProcessingUrl("/do-processing")
		.defaultSuccessUrl("/user/index")
		.and().csrf().disable();
		httpSecurity.oauth2Login(oauth->{
			oauth.loginPage("/signin");
			oauth.successHandler(handler); 
		});
		
		
		return httpSecurity.build();
	}
	/*
	 * serveral method to configure behaviour of login page
	 * 1. loginPage("url"):- to add custom login page
	 * 2. loginProcessingUrl("url") :- where we want to submit username and password
	 * 3. defaultSuccessUrl("url"):- use to give landing page after successful login
	 * 4. failureUrl("url/"):- if unsuccessful login then we can specify url
	 * ex: see above
	 */

}
