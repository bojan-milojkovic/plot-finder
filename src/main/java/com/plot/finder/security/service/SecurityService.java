package com.plot.finder.security.service;

import com.plot.finder.security.dto.CredentialsDTO;

public interface SecurityService {

	String generateTokenForUser(CredentialsDTO credentials);
}