package com.plot.finder.user.service;

import java.util.List;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.user.entity.UserDTO;

public interface UserService {

	List<UserDTO> getAll();
	UserDTO getOneById(Long id) throws MyRestPreconditionsException;
	UserDTO getOneByUsername(String username) throws MyRestPreconditionsException;
	UserDTO getOneByEmail(String email) throws MyRestPreconditionsException;
	UserDTO getOneByMobile(String mobile) throws MyRestPreconditionsException;
	
	void deleteUser(Long id) throws MyRestPreconditionsException;
	UserDTO create(final UserDTO model) throws MyRestPreconditionsException;
}
