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

import com.plot.finder.util.RestPreconditions;

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
		
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Cors-Origin", "*");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, X-My-Security-Token, Access-Cors-Origin, Access-Control-Allow-Methods, Access-Control-Allow-Origin, Authorization, Origin, Content-type, Version");
        response.addHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-type");
        
        if(!(request.getRequestURI().contains("roles") || request.getRequestURI().contains("refresh"))){
        	String token = request.getHeader("X-My-Security-Token");
        	String username = jwtTokenUtil.getUsernameFromToken(token);

        	if (RestPreconditions.checkString(username) && SecurityContextHolder.getContext().getAuthentication()==null && jwtTokenUtil.validateToken(token)){
        		UsernamePasswordAuthenticationToken 
        		authentication = new UsernamePasswordAuthenticationToken(
		                				username, 
		                				null, 
		                				jwtTokenUtil.getAuthoritiesFromToken(token));
        		
        		SecurityContextHolder.getContext().setAuthentication(authentication);
        	}	
        }
        
        chain.doFilter(request, response);
	}
}