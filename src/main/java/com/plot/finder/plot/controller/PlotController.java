package com.plot.finder.plot.controller;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.images.storage.StorageService;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.service.PlotService;

@RestController
@RequestMapping(value="/plot")
public class PlotController {

	private PlotService plotServiceImpl;
	private StorageService storageServiceImpl;
	private ObjectMapper objectMapper;
	
	@Autowired
	public PlotController(PlotService plotServiceImpl, StorageService storageServiceImpl, ObjectMapper objectMapper) {
		this.plotServiceImpl = plotServiceImpl;
		this.storageServiceImpl = storageServiceImpl;
		this.objectMapper = objectMapper;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody PlotDTO getOneById(@PathVariable("id") Long id) throws MyRestPreconditionsException {
		return plotServiceImpl.findOneById(id);
	}
	
	@RequestMapping(value="", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
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
	
	@RequestMapping(value="/img/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageLarge(@PathVariable("id") final Long id,
																@RequestParam(value="name", required=true) String name,
																HttpServletRequest request) throws MyRestPreconditionsException{
		return storageServiceImpl.getImage(plotServiceImpl.getImage(id, name, false), request);
	}
	
	@RequestMapping(value="/thumb/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageThumb(@PathVariable("id") final Long id,
																@RequestParam(value="name", required=true) String name,
																HttpServletRequest request) throws MyRestPreconditionsException{
		return storageServiceImpl.getImage(plotServiceImpl.getImage(id, name, true), request);
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
	public @ResponseBody PlotDTO editPlotWithPictures(@RequestParam(value="json", required=true) final String json,
													  @RequestParam(value="file1", required=true) final MultipartFile file1,
													  @RequestParam(value="file2", required=false) final MultipartFile file2,
													  @RequestParam(value="file3", required=false) final MultipartFile file3,
													  @RequestParam(value="file4", required=false) final MultipartFile file4, 
													  @PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		try{
			PlotDTO model = objectMapper.readValue(json, PlotDTO.class);

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
}