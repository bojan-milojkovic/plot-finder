package com.plot.finder.watched.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.watched.entity.WatchedDTO;
import com.plot.finder.watched.service.WatchedService;

@RestController
@RequestMapping(value="/watch")
public class WatchedController {

	@Autowired
	private WatchedService watchedServiceImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(WatchedController.class);
	
	@RequestMapping(value="", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public WatchedDTO addEditArea(WatchedDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" POST new watch-area");
		return watchedServiceImpl.addEdit(model, principal.getName());
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public WatchedDTO getWatchedArea(Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" GET his/hers watch-area");
		return watchedServiceImpl.getWatched(principal.getName());
	}
	
	@RequestMapping(value="", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteWatchedArea(Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" DELETE watch-area");
		watchedServiceImpl.deleteArea(principal.getName());
	}
}