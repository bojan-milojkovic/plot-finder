package com.plot.finder.admin.service;

import com.plot.finder.exception.MyRestPreconditionsException;

public interface AdminService {

	void lockUser(final Long id, final String admin) throws MyRestPreconditionsException;
	
	void unlockUser(final String username, final String admin) throws MyRestPreconditionsException;
	
	void makeUserAdmin(final Long id, final String admin) throws MyRestPreconditionsException;
	
	void removeAdminFromUser(final Long id, final String admin)throws MyRestPreconditionsException;
}
