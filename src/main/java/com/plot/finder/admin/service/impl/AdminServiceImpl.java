package com.plot.finder.admin.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.admin.service.AdminService;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.security.entities.RoleJPA;
import com.plot.finder.security.entities.UserHasRolesJPA;
import com.plot.finder.security.repository.RoleRepository;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Service
public class AdminServiceImpl implements AdminService {
	
	private UserRepository userRepo;
	private PlotRepository plotRepo;
	private RoleRepository roleRepo;
	private EmailUtil emailUtil;
	
	@Autowired
	public AdminServiceImpl(UserRepository userRepo, PlotRepository plotRepo, RoleRepository roleRepo, EmailUtil emailUtil) {
		this.userRepo = userRepo;
		this.plotRepo = plotRepo;
		this.roleRepo = roleRepo;
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
		
		RestPreconditions.assertTrue(!(jpa.getUserHasRolesJpa()
										  .stream()
										  .filter(j -> j.getRoleJpa().getRoleId()==2)
										  .findFirst())
									 .isPresent(), "Admin user adminize", "This user is already an admin");
		
		// security roles :
		UserHasRolesJPA uhrJpa = new UserHasRolesJPA();
		RoleJPA rJpa = roleRepo.getOne(2L);
		
		uhrJpa.setRoleJpa(rJpa);
		rJpa.getUserHasRolesJpa().add(uhrJpa);
		
		uhrJpa.setUserSecurityJpa(jpa);
		jpa.getUserHasRolesJpa().add(uhrJpa);
		
		userRepo.save(jpa);
	}
	
	//TODO : test this
	public void removeAdminFromUser(final Long id, final String admin)throws MyRestPreconditionsException{
		UserJPA jpa = RestPreconditions.checkNotNull(userRepo.getOne(id), 
				"Admin user de-adminize", "Cannot find user with id = "+id);
		
		RestPreconditions.assertTrue(!jpa.getUsername().equals(admin), 
				"Admin user de-adminize", "You cannot remove your own admin privileges");
		
		Optional<UserHasRolesJPA> uhrJpa = jpa.getUserHasRolesJpa()
									.stream()
									.filter(j -> j.getRoleJpa().getRoleId()==2)
									.findFirst();
		
		RestPreconditions.assertTrue(uhrJpa.isPresent(), "Admin user de-adminize", "User with id="+id+" has no admin privileges for you to remove");
		
		jpa.getUserHasRolesJpa().remove(uhrJpa.get());

		userRepo.save(jpa);
	}
}