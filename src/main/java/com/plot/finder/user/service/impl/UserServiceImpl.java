package com.plot.finder.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.user.service.UserService;
import com.plot.finder.util.RestPreconditions;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	public List<UserDTO> getAll(){
		return userRepo.findAll().stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}
	
	public UserDTO getOneById(Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Find user by id failed", "That user does not exist in our database"));
	}
	
	public UserDTO getOneByUsername(String username) throws MyRestPreconditionsException {
		RestPreconditions.checkStringIsValid(username, "Find user by username failed");
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByUsername(username), 
				"Find user by username failed", "That user does not exist in our database"));
	}
	
	public UserDTO getOneByEmail(String email) throws MyRestPreconditionsException {
		RestPreconditions.checkStringIsValid(email, "Find user by email failed");
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByEmail(email), 
				"Find user by email failed", "That user does not exist in our database"));
	}
	
	public UserDTO getOneByMobile(String mobile) throws MyRestPreconditionsException {
		RestPreconditions.checkStringIsValid(mobile, "Find user by mobile phone number failed");
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByMobile(mobile), 
				"Find user by mobile failed", "That user does not exist in our database"));
	}
	
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setEmail(jpa.getEmail());
		model.setFirstName(jpa.getFirstName());
		model.setId(jpa.getId());
		model.setLastName(jpa.getLastName());
		model.setMobile(jpa.getMobile());
		model.setUsername(jpa.getUsername());
		model.setRegisterred(jpa.getRegistration());
		
		return model;
	}
	
	public UserJPA convertModelToJpa(UserDTO model) {
		UserJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new UserJPA();
			
			jpa.setActive(true);
			jpa.setNotLocked(true);
			jpa.setLastLogin(LocalDateTime.now());
			jpa.setLastPasswordChange(LocalDateTime.now());
		} else {
			jpa = userRepo.getOne(model.getId());
		}
		jpa.setLastUpdate(LocalDateTime.now());
		
		if(RestPreconditions.checkString(model.getEmail())) {
			jpa.setEmail(model.getEmail());
		}
		if(RestPreconditions.checkString(model.getFirstName())) {
			jpa.setFirstName(model.getFirstName());
		}
		if(RestPreconditions.checkString(model.getLastName())) {
			jpa.setLastName(model.getLastName());
		}
		if(RestPreconditions.checkString(model.getMobile())) {
			jpa.setMobile(model.getMobile());
		}
		return jpa;
	}
}