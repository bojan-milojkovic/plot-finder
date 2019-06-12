package com.plot.finder.plot.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.Vertice;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.util.RestPreconditions;

@RestController
@RequestMapping(value="/plot")
public class PlotController {

	private PlotService plotServiceImpl;
	private ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	@Autowired
	public PlotController(PlotService plotServiceImpl, ObjectMapper objectMapper) {
		this.plotServiceImpl = plotServiceImpl;
		this.objectMapper = objectMapper;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody PlotDTO getOneById(@PathVariable("id") Long id, Principal p) throws MyRestPreconditionsException {
		logger.debug("User "+p.getName()+" GET for plot with id = "+id);
		return plotServiceImpl.findOneById(id);
	}
	
	@RequestMapping(value="", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody PlotDTO addNew(@RequestBody @Valid PlotDTO model, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" POST new plot, no images");
		return plotServiceImpl.addNew(model, principal.getName());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody PlotDTO editPlot(@RequestBody @Valid PlotDTO model, @PathVariable("id") final Long id, Principal principal) 
			throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" PATCH plot with id = "+id+", no images");
		return plotServiceImpl.edit(model, id, principal.getName());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void delete(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" DELETE plot with id = "+id);
		plotServiceImpl.delete(id, principal.getName(), true);
	}
	
	@RequestMapping(value="/ren/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void renew(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" renew plot add for plot id = "+id);
		plotServiceImpl.renewPlotAdd(id, principal.getName());
	}
	
	@RequestMapping(value="/img/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageLarge(@PathVariable("id") final Long id,
																@RequestParam(value="name", required=true) String name,
																HttpServletRequest request,
																Principal p) throws MyRestPreconditionsException{
		logger.debug("User "+p.getName()+" GET large image "+name+" for plot "+id);
		return plotServiceImpl.getImage(id, name, false, request);
	}
	
	@RequestMapping(value="/thumb/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resource> getImageThumb(@PathVariable("id") final Long id,
																@RequestParam(value="name", required=true) String name,
																HttpServletRequest request,
																Principal p) throws MyRestPreconditionsException{
		logger.debug("User "+p.getName()+" GET large image "+name+" for plot "+id);
		return plotServiceImpl.getImage(id, name, true, request);
	}
	
	// save with image(s)
	@RequestMapping(value = "", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody PlotDTO addPlotWithPictures(@RequestParam(value="json", required=true) final String json,
			@RequestParam(value="file1", required=true) final MultipartFile file1,
			@RequestParam(value="file2", required=false) final MultipartFile file2,
			@RequestParam(value="file3", required=false) final MultipartFile file3,
			@RequestParam(value="file4", required=false) final MultipartFile file4,
			Principal principal) throws MyRestPreconditionsException {
		
		logger.debug("User "+principal.getName()+" POST new plot with images");
		try{
			PlotDTO model = objectMapper.readValue(json, PlotDTO.class);

			model.setFile1(file1);
			model.setFile2(file2);
			model.setFile3(file3);
			model.setFile4(file4);
			
			return plotServiceImpl.addNew(model, principal.getName());
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add Plot error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	// edit with images :
	@RequestMapping(value="/{id}", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE/*, headers="Content-Type=multipart/form-data"*/)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody PlotDTO editPlotWithPictures(@RequestParam(value="json", required=false) final String json,
													  @RequestParam(value="file1", required=true) final MultipartFile file1,
													  @RequestParam(value="file2", required=false) final MultipartFile file2,
													  @RequestParam(value="file3", required=false) final MultipartFile file3,
													  @RequestParam(value="file4", required=false) final MultipartFile file4, 
													  @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		
		logger.debug("User "+principal.getName()+" POST(EDIT) plot "+id+", with images");
		try{
			PlotDTO model = new PlotDTO();
			if(RestPreconditions.checkString(json)){
				model = objectMapper.readValue(json, PlotDTO.class);
			}
			model.setFile1(file1);
			model.setFile2(file2);
			model.setFile3(file3);
			model.setFile4(file4);
			
			return plotServiceImpl.edit(model, id, principal.getName());
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add Plot error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	@RequestMapping(value="/fbv", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<PlotDTO> findPlotsByCoordinates(@RequestBody final List<Vertice> list, Principal p) throws MyRestPreconditionsException{
		logger.debug("User "+p.getName()+" POST find plots by coordinates");
		RestPreconditions.assertTrue(list.size()==2, "Find plots by coordinates error", "You must input exactly 2 vertices");
		return plotServiceImpl.findPlotsByCoordinates(list.get(0), list.get(1));
	}
	
	@RequestMapping(value="/fbwa", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<PlotDTO> findPlotsInWatchedArea(Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" GET plots in user's watched area");
		return plotServiceImpl.findPlotsInUserWatchedArea(principal.getName());
	}
	
	@RequestMapping(value="/fbp", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<PlotDTO> findPlotsByProperties(@RequestBody final PlotDTO model, Principal p) throws MyRestPreconditionsException{
		logger.debug("User "+p.getName()+" POST find plots by plot properties");
		return plotServiceImpl.findPlotsByProperties(model);
	}
}