package com.plot.finder.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;

}
