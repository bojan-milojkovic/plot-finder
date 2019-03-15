package com.plot.finder.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.dto.CredentialsDTO;
import com.plot.finder.security.service.SecurityService;

@Controller
public class SecurityController {

	@Autowired
	private SecurityService securityServiceImpl;
	
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateToken(@RequestBody CredentialsDTO credentials){
		return securityServiceImpl.generateTokenForUser(credentials);
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String refreshToken(@RequestHeader HttpHeaders httpHeaders) throws MyRestPreconditionsException{
		return securityServiceImpl.refreshToken(httpHeaders);
	}
}