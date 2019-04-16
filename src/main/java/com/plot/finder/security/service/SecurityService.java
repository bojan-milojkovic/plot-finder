package com.plot.finder.security.service;

import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.security.dto.CredentialsDTO;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials) throws MyRestPreconditionsException;
	
	String refreshToken(final String token) throws MyRestPreconditionsException;
}