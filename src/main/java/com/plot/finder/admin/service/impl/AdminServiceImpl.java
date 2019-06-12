package com.plot.finder.admin.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.admin.service.AdminService;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Service
public class AdminServiceImpl implements AdminService {
	
	private UserRepository userRepo;
	private PlotRepository plotRepo;
	private EmailUtil emailUtil;
	
	@Autowired
	public AdminServiceImpl(UserRepository userRepo, PlotRepository plotRepo, EmailUtil emailUtil) {
		this.userRepo = userRepo;
		this.plotRepo = plotRepo;
		this.emailUtil = emailUtil;
	}

	public void lockUser(final Long id, final String admin) throws MyRestPreconditionsException{
		RestPreconditions.checkId(id);
		
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Admin user lock", "Cannot find user with id = "+id);
		
		RestPreconditions.assertTrue(!jpa.getUsername().equals(admin), "Admin user lock", "You cannot lock yourself");
		
		jpa.setNotLocked(false);
		jpa.setIdentifier(UUID.randomUUID().toString());
		jpa.setLastUpdate(LocalDateTime.now());
		
		emailUtil.userLocked(jpa);
		
		userRepo.save(jpa);
	}
	
	public void unlockUser(final String username, final String admin) throws MyRestPreconditionsException{
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.findOneByUsername(username), 
				"Admin user unlock", "Cannot find user with username = "+username);
		
		RestPreconditions.assertTrue(!jpa.getUsername().equals(admin), "Admin user unlock", "You cannot unlock yourself");
		
		jpa.setNotLocked(true);
		jpa.setIdentifier(null);
		jpa.setLastUpdate(LocalDateTime.now());
		
		emailUtil.userUnlocked(jpa);
		
		userRepo.save(jpa);
	}
	
	public void makeUserAdmin(final Long id, final String admin) throws MyRestPreconditionsException{
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Admin user adminize", "Cannot find user with id = "+id);
		
		RestPreconditions.assertTrue(!jpa.getUsername().equals(admin), "Admin user adminize", "You are already an admin");
		
		RestPreconditions.assertTrue(!jpa.checkIfUserHasAdminRole(), "Admin user adminize", "This user is already an admin");
		
		// security roles :
		jpa.setAuthorities(jpa.getAuthorities()+"#ROLE_ADMIN");
		
		userRepo.save(jpa);
	}
	
	public void removeAdminFromUser(final Long id, final String admin)throws MyRestPreconditionsException{
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Admin user de-adminize", "Cannot find user with id = "+id);
		
		RestPreconditions.assertTrue(!jpa.getUsername().equals(admin), 
				"Admin user de-adminize", "You cannot remove your own admin privileges");
		RestPreconditions.assertTrue(jpa.checkIfUserHasAdminRole(), 
				"Admin user de-adminize", "User with id="+id+" has no admin privileges for you to remove");
		
		jpa.setAuthorities("ROLE_USER");

		userRepo.save(jpa);
	}
}