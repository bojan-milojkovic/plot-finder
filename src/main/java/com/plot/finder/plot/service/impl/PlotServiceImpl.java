package com.plot.finder.plot.service.impl;

import java.util.stream.Collectors;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.util.RestPreconditions;

@Service
public class PlotServiceImpl implements PlotService {

	private PlotRepository plotRepo;
	
	@Autowired
	public PlotServiceImpl(PlotRepository plotRepo) {
		this.plotRepo = plotRepo;
	}
	
	// convert model to jpa :
	private PlotDTO convertJpaToModel(final PlotJPA jpa) throws MyRestPreconditionsException {
		PlotDTO model = new PlotDTO();
		
		model.setId(jpa.getId());
		
		model.setVertices(Arrays.asList(jpa.getPolygon().split("#"))
				.stream()
				.map(v -> { return new PlotDTO.Vertice((String) v); })
				.collect(Collectors.toList()));
		
		return model;
	}
	
	private PlotJPA convertModelToJpa(final PlotDTO model) {
		PlotJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new PlotJPA();
		} else {
			jpa = plotRepo.getOne(model.getId());
		}
		
		return jpa;
	}
	
	// find plot by id :
	public PlotDTO findOneById(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		return convertJpaToModel(RestPreconditions.checkNotNull(plotRepo.getOne(id),"Finf plot error","Plot with id = "+id+" doesn't exist"));
	}
	
	public PlotDTO addNew(PlotDTO model) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Create new plot error", "You cannot create new plot with an empty object in request.");
		model.setId(null); // just in case
		
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			
			//TODO: check that it doesn't overlap with other plots in db :
			
			return convertJpaToModel(plotRepo.save(convertModelToJpa(model)));
		} else {
			throw new MyRestPreconditionsException("Create new plot error","Array of plot vertices is missing");
		}
	}
	
	public PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Edit plot error", "You cannot edit plot with an empty object in request.");
		RestPreconditions.checkId(id);
		
		model.setId(id);
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			// check plot exists
			PlotJPA jpa = RestPreconditions.checkNotNull(plotRepo.getOne(id), 
					"Edit plot error", "You are trying to edit a plot that doesn't exist in our database");
			// check user is editing his plot and not someone else's
			
			//TODO: check that it doesn't overlap with other plots in db
			
			return convertJpaToModel(plotRepo.save(convertModelToJpa(model)));
		} else {
			throw new MyRestPreconditionsException("Edit plot error","Array of new plot vertices is missing");
		}
	}
	
	public void delete(final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		PlotJPA jpa = RestPreconditions.checkNotNull(plotRepo.getOne(id), 
				"Delete plot error", "Plot with id = "+id+" doesn't exist");
		
		// check user is editing his plot and not someone else's
		
		plotRepo.delete(jpa);
	}
}