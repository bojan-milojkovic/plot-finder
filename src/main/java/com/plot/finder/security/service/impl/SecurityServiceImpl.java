package com.plot.finder.security.service.impl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.JwtTokenUtil;
import com.plot.finder.security.dto.CredentialsDTO;
import com.plot.finder.security.service.SecurityService;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public String generateTokenForUser(CredentialsDTO credentials) throws MyRestPreconditionsException{
		
		// check username :
		UserJPA jpa = RestPreconditions.checkNotNull(userRepository.findOneByUsername(credentials.getUsername()),
									"Authentication failed", "No user found for these credentials");
		
		// check password - BCrypt.checkpw(password_plaintext, stored_hash)
		RestPreconditions.assertTrue(BCrypt.checkpw(credentials.getPassword(), jpa.getPassword()), 
									"Authentication failed", "Passwords do not match");
		
		String token = jwtTokenUtil.generateToken(new User(
					jpa.getUsername(),
					"",
					jpa.isActive(),
					true,
					true,
					jpa.isNotLocked(),
					jpa.getRoleList()
				));
		
		// update last login datetime
		jpa.setLastLogin(LocalDateTime.now());
		userRepository.save(jpa);
		
		return token;
	}
	
	@Override
	public String refreshToken(final String token) throws MyRestPreconditionsException {
		return jwtTokenUtil.refreshToken(token);
	}
}