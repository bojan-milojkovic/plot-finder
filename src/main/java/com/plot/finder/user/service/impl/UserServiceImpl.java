package com.plot.finder.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.user.service.UserService;
import com.plot.finder.util.RestPreconditions;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;
	private EmailUtil emailUtil;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepo, EmailUtil emailUtil) {
		this.userRepo = userRepo;
		this.emailUtil = emailUtil;
	}

	public List<UserDTO> getAll(){
		return userRepo.findAll().stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}
	
	public UserDTO getOneById(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Find user by id failed", "That user does not exist in our database"));
	}
	
	public UserDTO getOneByUsername(final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkStringIsValid(username, "Find user by username failed");
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByUsername(username), 
				"Find user by username failed", "That user does not exist in our database"));
	}
	
	public UserDTO getOneByEmail(final String email) throws MyRestPreconditionsException {
		RestPreconditions.checkStringIsValid(email, "Find user by email failed");
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByEmail(email), 
				"Find user by email failed", "That user does not exist in our database"));
	}
	
	public void delete(Long id, String username) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		RestPreconditions.assertTrue(
				// check user exists
				(RestPreconditions.checkNotNull(userRepo.getOne(id), "Delete user failed","No user found for id = "+id)) // Returns UserJPA
				// check user is not deleting himself and not someone else
				.getUsername().equals(username), "Delete user failed", 
				"You are trying to delete someone else's user");
		
		userRepo.deleteById(id);
	}
	
	public UserDTO create(final UserDTO model) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "User crete error", "Add new user cannot be done without the user object");
		model.setId(null);
		if(checkPostDataPresent(model)) {
			// check username unique :
			RestPreconditions.assertTrue(userRepo.findOneByUsername(model.getUsername())==null, "User create error", 
						"User with that username already exists");
			// check email format :
			RestPreconditions.verifyStringFormat(model.getEmail(), "^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$",
					"User create error","email is in invalid format");
			// check email unique :
			RestPreconditions.assertTrue(userRepo.findOneByEmail(model.getEmail())==null, "User create error", 
					"User with that email already exists");
			
			return convertJpaToModel(userRepo.save(convertModelToJpa(model)));
		}
		
		MyRestPreconditionsException e = new MyRestPreconditionsException("User create error", 
				"Some mandatory fields are missing from request body");
		if(RestPreconditions.checkString(model.getUsername())) {
			e.getErrors().add("Username is mandatory");
		}
		if(RestPreconditions.checkString(model.getPassword())) {
			e.getErrors().add("Password is mandatory");
		}
		if(RestPreconditions.checkString(model.getEmail())) {
			e.getErrors().add("Email is mandatory");
		}
		throw e;
	}
	
	private void checkProperty(String property, String propertyName, Long id, String regex, UserJPA tmp) 
							throws MyRestPreconditionsException {
		if(RestPreconditions.checkString(property)) { // here, it must be RestPreconditions.checkString(...) 
			RestPreconditions.verifyStringFormat(property, regex, "User edit error", 
					propertyName+" is in invalid format");
			
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==id,
						"User edit error", propertyName+" you are adding belongs to another user.");
			}
		}
	}
	
	public UserDTO edit(UserDTO model, Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		model.setId(id);
		
		if(checkPatchDataPresent(model)) {
			RestPreconditions.assertTrue(
					// check username exists :
					(RestPreconditions.checkNotNull(userRepo.findOneByUsername(model.getUsername()), "User edit error", 
					"User with username "+model.getUsername()+" does not exist in our database.")) // returns UserJPA
						// check id matches
						.getId()==id, "User edit error", 
						"You are trying to edit someone else's user.");

			// check email :
			checkProperty(model.getEmail(),
						  "email",
						  id,
						  "^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$",
						  userRepo.findOneByEmail(model.getEmail()) );
			
			UserJPA jpa = userRepo.save(convertModelToJpa(model));
			
			return convertJpaToModel(jpa);
		} else {
			throw new MyRestPreconditionsException("Edit user error",
					"You must provide some editable data.");
		}
	}
	
	public void changePassword(UserDTO model, String username) throws MyRestPreconditionsException {
		{ 	// input data basic checks :
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("Change password error", "Input information is invalid.");
	
			if(!(model.getId()!=null && model.getId()>0)) {
				ex.getErrors().add("Invalid user id");
			}
			// check passwords :
			if(!RestPreconditions.checkString(model.getPassword())) {
				ex.getErrors().add("Original password is mandatory");
			}
			if(!RestPreconditions.checkString(model.getNewPassword())) {
				ex.getErrors().add("New password is mandatory");
			}
			if(model.getPassword().equals(model.getNewPassword())) {
				ex.getErrors().add("Old password and new password should be different.");
			}
			
			if(!ex.getErrors().isEmpty()){
				throw ex;
			}
		} // ex no longer exists from here on
		
		// check user exists for that id
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.getOne(model.getId()), 
				"Change password error", "No user found for that id");
		// check that usernames match
		RestPreconditions.assertTrue(username.equals(jpa.getUsername()), 
				"Access violation !!!","You are trying to change someone elses's password");
		// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
		RestPreconditions.assertTrue(BCrypt.checkpw(model.getPassword(), jpa.getPassword()), 
				"Change password error","Your original password value does not match with the password in DB");
		
		jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
		jpa.setLastPasswordChange(LocalDateTime.now());
		jpa.setLastUpdate(LocalDateTime.now());
		userRepo.save(jpa);
	}
	
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setId(jpa.getId());
		model.setUsername(jpa.getUsername());
		model.setRegisterred(jpa.getRegistration());
		model.setEmail(jpa.getEmail());
		
		return model;
	}
	
	public UserJPA convertModelToJpa(UserDTO model) {
		UserJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new UserJPA();
			
			jpa.setActive(false);
			jpa.setNotLocked(true);
			
			jpa.setUsername(model.getUsername());
			jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
			String identifier = UUID.randomUUID().toString();
			jpa.setIdentifier(identifier);
			
			jpa.setRegistration(LocalDateTime.now());
			jpa.setLastLogin(LocalDateTime.now());
			jpa.setLastPasswordChange(LocalDateTime.now());
			
			jpa.setAuthorities("ROLE_USER");
			
			// to get user_id for hashCode in UserHasRolesJPA :
			jpa = userRepo.save(finishConvertingModelToJpa(model, jpa));
			
			// security roles :
			/*UserHasRolesJPA uhrJpa = new UserHasRolesJPA();
			RoleJPA rJpa = roleRepo.getOne(1L);
			
			uhrJpa.setRoleJpa(rJpa);
			rJpa.getUserHasRolesJpa().add(uhrJpa);
			
			uhrJpa.setUserSecurityJpa(jpa);
			jpa.getUserHasRolesJpa().add(uhrJpa);*/
			
			emailUtil.confirmRegistration(identifier, model.getEmail(), model.getEmail());
		} else {
			jpa = finishConvertingModelToJpa(model, userRepo.getOne(model.getId()));
		}
		
		return jpa;
	}
	
	private UserJPA finishConvertingModelToJpa(final UserDTO model, UserJPA jpa){
		jpa.setLastUpdate(LocalDateTime.now());
		
		if(RestPreconditions.checkString(model.getEmail())) {
			jpa.setEmail(model.getEmail());
		}
		return jpa;
	}
	
	private boolean checkPostDataPresent(UserDTO model) {
		return RestPreconditions.checkString(model.getUsername()) &&
				RestPreconditions.checkString(model.getPassword()) &&
				RestPreconditions.checkString(model.getEmail());
	}
	
	private boolean checkPatchDataPresent(UserDTO model) {
		return RestPreconditions.checkString(model.getEmail());
	}

	public String activateUser(String key) throws MyRestPreconditionsException {
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.findByIdentifier(key), "User activation error", "Cannot find user with that activation key");
		
		jpa.setIdentifier(null);
		jpa.setActive(true);
		userRepo.save(jpa);
		
		return jpa.getUsername();
	}
}