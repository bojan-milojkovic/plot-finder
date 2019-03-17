package com.plot.finder.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CredentialsFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public CredentialsFilter(){
		super();
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, PATCH, GET, DELETE");
        /*response.setHeader("Access-Control-Max-Age", "3600");*/
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, X-My-Security-Token, Authorization, Origin, Content-Type, Version");
        response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");
        
        if(!request.getRequestURI().contains("roles") && !request.getRequestURI().contains("refresh")){
        	String token = request.getHeader("X-My-Security-Token");
        	String username = jwtTokenUtil.getUsernameFromToken(token);

        	if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null && jwtTokenUtil.validateToken(token)){
        		UsernamePasswordAuthenticationToken 
        		authentication = new UsernamePasswordAuthenticationToken(
		                				username, 
		                				jwtTokenUtil.getPasswordFromToken(token), 
		                				jwtTokenUtil.getAuthoritiesFromToken(token));
        		
        		SecurityContextHolder.getContext().setAuthentication(authentication);
        	}	
        }
        
        chain.doFilter(request, response);
	}

}