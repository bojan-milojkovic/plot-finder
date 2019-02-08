package com.plot.finder.plot.controller;

import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.service.PlotService;

@RestController("/plot")
public class PlotController {

	@Autowired
	private PlotService plotServiceImpl;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody PlotDTO getOneById(@PathVariable("id") Long id) throws MyRestPreconditionsException {
		return plotServiceImpl.findOneById(id);
	}
	
	@RequestMapping(value="", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody PlotDTO addNew(@RequestBody @Valid PlotDTO model, Principal principal) throws MyRestPreconditionsException {
		return plotServiceImpl.addNew(model, principal.getName());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody PlotDTO editPlot(@RequestBody @Valid PlotDTO model, @PathVariable("id") final Long id, Principal principal) 
			throws MyRestPreconditionsException {
		return plotServiceImpl.edit(model, id, principal.getName());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void delete(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		plotServiceImpl.delete(id, principal.getName());
	}
}