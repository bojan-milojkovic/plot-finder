package com.plot.finder.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;
import com.plot.finder.user.service.UserService;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> getAllUsers(){
		return userServiceImpl.getAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public UserDTO getUserById(@PathVariable("id") Long id) throws MyRestPreconditionsException{
		return userServiceImpl.getOneById(id);
	}
	
	@RequestMapping(value = "/un", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public UserDTO getUserByUsername(@RequestParam(value="username") final String username) throws MyRestPreconditionsException {
		return userServiceImpl.getOneByUsername(username);
	}
	
	@RequestMapping(value = "/e", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public UserDTO getUserByEmail(@RequestParam(value="email") final String email) throws MyRestPreconditionsException {
		return userServiceImpl.getOneByEmail(email);
	}
	
	@RequestMapping(value = "/m", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public UserDTO getUserByMobile(@RequestParam(value="mobile") final String mobile) throws MyRestPreconditionsException {
		return userServiceImpl.getOneByMobile(mobile);
	}
}