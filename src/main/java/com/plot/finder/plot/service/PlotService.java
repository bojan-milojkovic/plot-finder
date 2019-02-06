package com.plot.finder.plot.service;

import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;

public interface PlotService {
	
	PlotDTO findOneById(final Long id) throws MyRestPreconditionsException;
	
	PlotDTO addNew(PlotDTO model, final String username) throws MyRestPreconditionsException;

	PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException;
	
	void delete(final Long id, final String username) throws MyRestPreconditionsException;
}