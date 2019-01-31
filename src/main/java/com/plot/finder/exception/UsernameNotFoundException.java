package com.plot.finder.exception;

import org.springframework.security.core.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException{

		private static final long serialVersionUID = 1L;

		public UsernameNotFoundException(String msg) {
			super(msg);
		}

}