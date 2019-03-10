package com.plot.finder.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.admin.service.AdminService;
import com.plot.finder.exception.MyRestPreconditionsException;

@RestController
@RequestMapping("/adm")
public class AdminController {

	@Autowired
	private AdminService adminServiceImpl;
	
	@RequestMapping(value = "/l/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void lockUser(@PathVariable("id") final Long id) throws MyRestPreconditionsException{
		adminServiceImpl.lockUser(id);
	}
	
	@RequestMapping(value = "/u/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void unlockUser(@PathVariable("id") final String username) throws MyRestPreconditionsException{
		adminServiceImpl.unlockUser(username);
	}
	
	@RequestMapping(value = "/a/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void makeUserAdmin(@PathVariable("id") final Long id) throws MyRestPreconditionsException{
		adminServiceImpl.makeUserAdmin(id);
	}
	
	@RequestMapping(value = "/d/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void makeAdminUser(@PathVariable("id") final Long id) throws MyRestPreconditionsException{
		adminServiceImpl.removeAdminFromUser(id);
	}
}