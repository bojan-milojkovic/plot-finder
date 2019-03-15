package com.plot.finder.security.service;

import org.springframework.http.HttpHeaders;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.dto.CredentialsDTO;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials);
	
	String refreshToken(HttpHeaders httpHeaders) throws MyRestPreconditionsException;
}