package com.plot.finder.plot.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;

public interface PlotService {
	
	PlotDTO findOneById(final Long id) throws MyRestPreconditionsException;
	
	PlotDTO addNew(PlotDTO model, final String username) throws MyRestPreconditionsException;

	PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException;
	
	void delete(final Long id, final String username) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getImage(Long id, String name, boolean isThumbnail, HttpServletRequest request) throws MyRestPreconditionsException;
}