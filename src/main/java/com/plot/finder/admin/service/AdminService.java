package com.plot.finder.admin.service;

import com.plot.finder.exception.MyRestPreconditionsException;

public interface AdminService {

	void lockUser(final Long id) throws MyRestPreconditionsException;
	
	void unlockUser(final String username) throws MyRestPreconditionsException;
	
	void makeUserAdmin(final Long id) throws MyRestPreconditionsException;
	
	void removeAdminFromUser(final Long id)throws MyRestPreconditionsException;
}
