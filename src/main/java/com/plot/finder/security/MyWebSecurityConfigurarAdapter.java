package com.plot.finder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.plot.finder.security.service.BokiAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class MyWebSecurityConfigurarAdapter extends WebSecurityConfigurerAdapter {

	private BokiAuthenticationProvider bokiAuthenticationProvider;
	private CredentialsFilter credentialsFilter;
	
	@Autowired
	public MyWebSecurityConfigurarAdapter(BokiAuthenticationProvider bokiAuthenticationProvider,
			CredentialsFilter credentialsFilter) {
		super();
		this.bokiAuthenticationProvider = bokiAuthenticationProvider;
		this.credentialsFilter = credentialsFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// request handling
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/users").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/users/act/*").permitAll() // everyone should be able to activate their user account
			.antMatchers(HttpMethod.GET, "/users/**").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/users").permitAll()  // everyone should access register-api 
			.antMatchers(HttpMethod.PATCH, "/users/*").hasRole("USER")
			.antMatchers(HttpMethod.PATCH, "/users/cpw/*").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/users/*").hasRole("USER")
			
			.antMatchers(HttpMethod.GET, "/plot/fbva").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/plot/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/plot/img/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/plot/thumb/*").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/plot/ren/*").hasRole("USER")
			
			.antMatchers(HttpMethod.POST, "/plot/fbv").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/plot/fbp").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/plot").hasRole("USER")
			.antMatchers(HttpMethod.PATCH, "/plot/*").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/plot/*").hasRole("USER")
		
			.antMatchers(HttpMethod.POST, "/watch").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/watch").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/watch").hasRole("USER")
			
			.antMatchers(HttpMethod.GET, "/adm/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET, "/adm/**").hasRole("SUPERADMIN")
			
			.antMatchers(HttpMethod.POST, "/roles").permitAll() // everyone should access login-api
			.antMatchers(HttpMethod.POST, "/refresh").hasRole("USER") // users can refresh tokens
			;
		
		// disable csrf
		http.csrf().disable();
		
		// app session is stateless
		http.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);		
		
		http.addFilterBefore(credentialsFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false)
			.authenticationProvider(bokiAuthenticationProvider);
    }
}