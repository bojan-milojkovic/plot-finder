package com.plot.finder.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByPhone(mobile), 
				"Find user by mobile failed", "That user does not exist in our database"));
	}
	
	public void delete(Long id, String username) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		UserJPA tmp = userRepo.getOne(id);
		RestPreconditions.checkNotNull(tmp, "Delete user failed","No user found for id = "+id);
		RestPreconditions.assertTrue(tmp.getUsername().equals(username), "Delete user failed", 
				"You are trying to delete someone else's user");
		
		userRepo.deleteById(id);
	}
	
	public UserDTO create(final UserDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model!=null, "User crete error", "Add new user cannot be done without the user object");
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
			// check mobile format :
			RestPreconditions.verifyStringFormat(model.getPhone1(), "^([+][0-9]{1-3})?[0-9 -]+$", 
									"User create error","mobile number is in invalid format");
			// check mobile unique :
			RestPreconditions.assertTrue(userRepo.findOneByPhone(model.getPhone1())==null, "User create error", 
					"User with that mobile phone number already exists");
			
			if(model.getPhone2() != null) { // next line checks that it is not empty string
				// check mobile format :
				RestPreconditions.verifyStringFormat(model.getPhone2(), "^([+][0-9]{1-3})?[0-9 -]+$", 
										"User create error","mobile number is in invalid format");
				// check mobile unique :
				RestPreconditions.assertTrue(userRepo.findOneByPhone(model.getPhone2())==null, "User create error", 
						"User with that mobile phone number already exists");
			}
		
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
		if(RestPreconditions.checkString(model.getFirstName())) {
			e.getErrors().add("First name is mandatory");
		}
		if(RestPreconditions.checkString(model.getLastName())) {
			e.getErrors().add("Last name is mandatory");
		}
		if(RestPreconditions.checkString(model.getEmail())) {
			e.getErrors().add("Email is mandatory");
		}
		if(RestPreconditions.checkString(model.getPhone1())) {
			e.getErrors().add("User's phone number is mandatory");
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
			// check username (model.getUsername() is set in controller)
			{
				UserJPA tmp = userRepo.findOneByUsername(model.getUsername());
				RestPreconditions.checkNotNull(tmp, "User edit error", 
						"User with username "+model.getUsername()+" does not exist in our database.");
				RestPreconditions.assertTrue(tmp.getId()==id, "User edit error", 
						"You are trying to edit someone else's user.");
				// tmp ceases to exist  here
			}
			// check email :
			checkProperty(model.getEmail(),
						  "email",
						  id,
						  "^[^@]+@[^@.]+(([.][a-z]{3})|(([.][a-z]{2}){1,2}))$",
						  userRepo.findOneByEmail(model.getEmail()) );
			// check phone1 :
			checkProperty(model.getPhone1(),
					  "Phone number 1",
					  id,
					  "^([+][0-9]{1-3})?[0-9 -]+$",
					  userRepo.findOneByPhone(model.getPhone1()) );
			
			// check phone2 :
			checkProperty(model.getPhone2(),
					  "Phone number 2",
					  id,
					  "^([+][0-9]{1-3})?[0-9 -]+$",
					  userRepo.findOneByPhone(model.getPhone2()) );
			
			return convertJpaToModel(userRepo.save(convertModelToJpa(model)));
		} else {
			throw new MyRestPreconditionsException("Edit user error",
					"You must provide some editable data.");
		}
	}
	
	public void changePassword(UserDTO model, String username) throws MyRestPreconditionsException {
		{
			MyRestPreconditionsException ex = 
					new MyRestPreconditionsException("Change password error","request json is missing some elements.");
	
			if((model.getId()==null || (model.getId()!=null && model.getId()<0))) {
				ex.getErrors().add("Invalid or missing user id");
			}
			if(!RestPreconditions.checkString(model.getPassword())) {
				ex.getErrors().add("Original password is mandatory");
			}
			if(!RestPreconditions.checkString(model.getNewPassword())) {
				ex.getErrors().add("New password is mandatory");
			}
			
			if(!ex.getErrors().isEmpty()){
				throw ex;
			}
		}
		
		RestPreconditions.assertTrue(!model.getPassword().equals(model.getNewPassword()),
				"Change password error","Old password and new password should be different.");
		
		//check that user exists
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.findOneByUsername(username), 
				"Change password error : user you are changing the password for does not exist.");
		// check that ids match
		RestPreconditions.assertTrue(jpa.getId() == model.getId(), 
				"Access violation !!!","You are trying to change someone elses's password");
		// password is verified with : BCrypt.checkpw(password_plaintext, stored_hash)
		RestPreconditions.assertTrue(BCrypt.checkpw(model.getPassword(), jpa.getPassword()), 
				"Change password error","Your entry for original password does not match with the DB value");
		
		jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
		userRepo.save(jpa);
	}
	
	public UserDTO convertJpaToModel(UserJPA jpa) {
		UserDTO model = new UserDTO();
		
		model.setEmail(jpa.getEmail());
		model.setFirstName(jpa.getFirstName());
		model.setId(jpa.getId());
		model.setLastName(jpa.getLastName());
		model.setPhone1(jpa.getPhone1());
		model.setPhone2(jpa.getPhone2());
		model.setUsername(jpa.getUsername());
		model.setRegisterred(jpa.getRegistration());
		
		return model;
	}
	
	public UserJPA convertModelToJpa(UserDTO model) {
		UserJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new UserJPA();
			
			//TODO : email user activation link
			jpa.setActive(true);
			jpa.setNotLocked(true);
			jpa.setLastLogin(LocalDateTime.now());
			jpa.setLastPasswordChange(LocalDateTime.now());
			jpa.setUsername(model.getUsername());
			jpa.setPassword(BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
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
		if(RestPreconditions.checkString(model.getPhone1())) {
			jpa.setPhone1(model.getPhone1());
		}
		if(RestPreconditions.checkString(model.getPhone2())) {
			jpa.setPhone2(model.getPhone2());
		}
		return jpa;
	}
	
	private boolean checkPostDataPresent(UserDTO model) {
		return RestPreconditions.checkString(model.getUsername()) &&
				RestPreconditions.checkString(model.getPassword()) &&
				RestPreconditions.checkString(model.getFirstName()) &&
				RestPreconditions.checkString(model.getLastName()) &&
				RestPreconditions.checkString(model.getEmail()) &&
				RestPreconditions.checkString(model.getPhone1());
	}
	
	private boolean checkPatchDataPresent(UserDTO model) {
		return RestPreconditions.checkString(model.getFirstName()) ||
				RestPreconditions.checkString(model.getLastName()) ||
				RestPreconditions.checkString(model.getEmail()) ||
				RestPreconditions.checkString(model.getPhone1()) ||
				RestPreconditions.checkString(model.getPhone2());
	}
}