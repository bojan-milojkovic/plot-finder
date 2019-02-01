package com.plot.finder.security.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import com.plot.finder.exception.UsernameNotFoundException;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Component
public class BokiAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName();
		
		if(RestPreconditions.checkString(username)){
			UserJPA jpa = userRepo.findOneByUsername(username);
			
			if(jpa!=null){
				String password = auth.getCredentials().toString();
				
				if(BCrypt.checkpw(password, jpa.getPassword())){

					@SuppressWarnings("unchecked")
					List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

					return new UsernamePasswordAuthenticationToken(
							jpa.getUsername(),
							null,
							authorities);
				}
				throw new UsernameNotFoundException("Passwords is missing or invalid.");
			}
			throw new UsernameNotFoundException("There is no user with username = "+username);
		}
		
		throw new UsernameNotFoundException("You did not provide a username.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
