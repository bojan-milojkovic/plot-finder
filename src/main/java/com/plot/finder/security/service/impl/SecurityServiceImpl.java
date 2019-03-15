package com.plot.finder.security.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.JwtTokenUtil;
import com.plot.finder.security.dto.CredentialsDTO;
import com.plot.finder.security.entities.RoleJPA;
import com.plot.finder.security.entities.UserHasRolesJPA;
import com.plot.finder.security.service.SecurityService;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public String generateTokenForUser(CredentialsDTO credentials){
		
		// check username :
		UserJPA jpa = userRepository.findOneByUsername(credentials.getUsername());
		if(jpa != null){
			// check password - BCrypt.checkpw(password_plaintext, stored_hash)
			if(BCrypt.checkpw(credentials.getPassword(), jpa.getPassword())){
				
				List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
				
				for(UserHasRolesJPA userRoles : jpa.getUserHasRolesJpa()){
					RoleJPA role = userRoles.getRoleJpa();
					
					authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
				}
				
				String token = jwtTokenUtil.generateToken(new User(
							jpa.getUsername(),
							credentials.getPassword(),
							jpa.isActive(),
							true,
							true,
							jpa.isNotLocked(),
							authorities
						));
				
				// update last login datetime
				jpa.setLastLogin(LocalDateTime.now());
				userRepository.save(jpa);
				
				return token;
			}
			
			return "Error - passwords do not match.";
		}
		
		return "Error - no user with those credentials.";
	}
	
	@Override
	public String refreshToken(HttpHeaders httpHeaders) throws MyRestPreconditionsException {
		return jwtTokenUtil.refreshToken(httpHeaders.toSingleValueMap().get("X-My-Security-Token"));
	}
}