package com.plot.finder.user.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;
import com.plot.finder.user.service.UserService;
import com.plot.finder.util.RestPreconditions;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<UserDTO> getAllUsers(Principal p){
		logger.debug("User "+p.getName()+" GET all users");
		return userServiceImpl.getAll();
	}
	
	@RequestMapping(value = "/act/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String activateUser(@PathVariable("key") final String key) throws MyRestPreconditionsException{
		logger.debug("User "+userServiceImpl.activateUser(key)+" activated");
		return "User activation successfull";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserById(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" GET user with id = "+id);
		return userServiceImpl.getOneById(id);
	}
	
	@RequestMapping(value = "/un", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserByUsername(@RequestParam(value="username") final String username, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" GET user with username = "+username);
		return userServiceImpl.getOneByUsername(username);
	}
	
	@RequestMapping(value = "/e", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody UserDTO getUserByEmail(@RequestParam(value="email") final String email, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" GET user with email = "+email);
		return userServiceImpl.getOneByEmail(email);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody UserDTO createNewUser(@RequestBody @Valid UserDTO model) throws MyRestPreconditionsException {
		logger.debug("POST new user with username = "+model.getUsername());
		return userServiceImpl.create(model);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUser(@PathParam("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" DELETE user with id = "+id);
		userServiceImpl.delete(id, principal.getName());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserDTO editUser(@PathParam("id") final Long id, @Valid UserDTO model, Principal principal) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model!=null, "Edit user error", "Edit user cannot be performed without the user object.");
		model.setUsername(principal.getName());
		logger.debug("User "+principal.getName()+" PATCH user with id = "+id);
		return userServiceImpl.edit(model, id);
	}
	
	// .../gh/users/cpw
	@RequestMapping(value="/cpw/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void changePassword(@RequestBody @Valid UserDTO model, @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model!=null, "User password edit error !!!", "You are sending a request without the object");
		model.setId(id);
		logger.debug("User "+principal.getName()+" PATCH(password change) for user with username = "+id);
		userServiceImpl.changePassword(model, principal.getName());
	}
}