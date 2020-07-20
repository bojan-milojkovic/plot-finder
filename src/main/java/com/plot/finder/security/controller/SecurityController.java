package com.plot.finder.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.JwtTokenUtil;
import com.plot.finder.security.dto.CredentialsDTO;
import com.plot.finder.security.service.SecurityService;

@Controller
public class SecurityController {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

	private SecurityService securityServiceImpl;
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	public SecurityController(SecurityService securityServiceImpl, JwtTokenUtil jwtTokenUtil) {
		this.securityServiceImpl = securityServiceImpl;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@RequestMapping(value = "/certificate", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String testCertificate(){
		return "OK";
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String generateToken(@RequestBody CredentialsDTO credentials) throws MyRestPreconditionsException{
		logger.debug("User "+credentials.getUsername()+" logging in ... ");
		return securityServiceImpl.generateTokenForUser(credentials);
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String refreshToken(@RequestHeader("X-My-Security-Token") String token) throws MyRestPreconditionsException{
		logger.debug("User "+jwtTokenUtil.getUsernameFromToken(token)+" refreshing the session ... ");
		return securityServiceImpl.refreshToken(token);
	}
}