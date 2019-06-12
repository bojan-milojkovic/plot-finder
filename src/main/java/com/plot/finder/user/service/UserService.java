package com.plot.finder.user.service;

import java.util.List;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;

public interface UserService {

	List<UserDTO> getAll();
	UserDTO getOneById(final Long id, final String name) throws MyRestPreconditionsException;
	UserDTO getOneByUsername(final String username, final String name) throws MyRestPreconditionsException;
	UserDTO getOneByEmail(final String email, final String name) throws MyRestPreconditionsException;
	UserDTO getOneByMobile(final String mobile, final String name) throws MyRestPreconditionsException;
	
	void delete(Long id, String username) throws MyRestPreconditionsException;
	UserDTO create(final UserDTO model) throws MyRestPreconditionsException;
	UserDTO edit(final UserDTO model, final Long id) throws MyRestPreconditionsException;
	String activateUser(final String key) throws MyRestPreconditionsException;
	void changePassword(UserDTO model, String username) throws MyRestPreconditionsException;
}
