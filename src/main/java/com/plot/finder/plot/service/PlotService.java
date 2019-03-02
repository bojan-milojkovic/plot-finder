package com.plot.finder.plot.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.Vertice;

public interface PlotService {
	
	PlotDTO findOneById(final Long id) throws MyRestPreconditionsException;
	
	PlotDTO addNew(PlotDTO model, final String username) throws MyRestPreconditionsException;

	PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException;
	
	void delete(final Long id, final String username, final boolean checks) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getImage(Long id, String name, boolean isThumbnail, HttpServletRequest request) throws MyRestPreconditionsException;

	void renewPlotAdd(final Long id, final String username) throws MyRestPreconditionsException;
	
	List<PlotDTO> findPlotsByCoordinates(final Vertice ll, final Vertice ur) throws MyRestPreconditionsException;
	
	List<PlotDTO> findPlotsByProperties(final PlotDTO model) throws MyRestPreconditionsException;
}