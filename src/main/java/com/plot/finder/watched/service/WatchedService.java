package com.plot.finder.watched.service;

import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.watched.entity.WatchedDTO;

public interface WatchedService {
	
	WatchedDTO getWatched(final String username) throws MyRestPreconditionsException;
	
	WatchedDTO addEdit(WatchedDTO model, final String username) throws MyRestPreconditionsException;
	
	void deleteArea(final String username) throws MyRestPreconditionsException;
	
	void checkNewPlotIsInsideAnArea(final PlotJPA entity);
}