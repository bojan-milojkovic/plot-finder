package com.plot.finder.plot.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.PlotDTO.Vertice;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Service
public class PlotServiceImpl implements PlotService {

	private PlotRepository plotRepo;
	private UserRepository userRepo;
	
	private static final Integer MAX_NUM_PLOTS = 3;
	
	@Autowired
	public PlotServiceImpl(PlotRepository plotRepo, UserRepository userRepo) {
		this.plotRepo = plotRepo;
		this.userRepo = userRepo;
	}
	
	// convert model to jpa :
	private PlotDTO convertJpaToModel(final PlotJPA jpa) throws MyRestPreconditionsException {
		PlotDTO model = new PlotDTO();
		
		model.setId(jpa.getId());
		
		model.setVertices(Arrays.asList(jpa.getPolygon().split("@"))
				.stream()
				.filter(v -> !((String)v).isEmpty())
				.map(v -> new PlotDTO.Vertice((String) v))
				.collect(Collectors.toList()));
		
		model.setAddress1(jpa.getAddress1());
		model.setAddress2(jpa.getAddress2());
		model.setCity(jpa.getCity());
		model.setCountry(jpa.getCountry());
		model.setDescription(jpa.getDescription());
		model.setTitle(jpa.getTitle());
		
		model.setGarage(jpa.isGarage());
		model.setGas(jpa.isGas());
		model.setInternet(jpa.isInternet());
		model.setPower(jpa.isPower());
		model.setSewer(jpa.isSewer());
		model.setWater(jpa.isWater());
		
		//model.setVertices(extractVertices(jpa.getPolygon()));
		
		return model;
	}
	
	/*private List<Vertice> extractVertices(String vertices){
		List<Vertice> result = new ArrayList<Vertice>();
		
		for(String v : vertices.split("@")) {
			if(!v.isEmpty()) {
				result.add(new Vertice(v));
			}
		}
		
		return result;
	}*/
	
	private PlotJPA setCoordinates(List<Vertice> vertices, PlotJPA jpa) {
		String tmp="";
		// jpa ll and ur x/y coordinates are already set
		
		for(Vertice v : vertices) {
			if(v.getLat() < jpa.getLl_y()) {
				jpa.setLl_y(v.getLat());
			}
			if(v.getLat() > jpa.getUr_y()) {
				jpa.setUr_y(v.getLat());
			}
			if(v.getLng() < jpa.getLl_x()) {
				jpa.setLl_x(v.getLng());
			}
			if(v.getLng() > jpa.getUr_x()) {
				jpa.setUr_x(v.getLng());
			}
			
			tmp+=(v.getLat().toString()+"#"+v.getLng().toString()+"@");
		}
		jpa.setPolygon(tmp);
		
		return jpa;
	}
	
	private PlotJPA convertModelToJpa(final PlotDTO model) {
		PlotJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new PlotJPA();
		} else {
			jpa = plotRepo.getOne(model.getId());
		}
		
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			jpa = setCoordinates(model.getVertices(), jpa);
		}
		
		if(model.isGarage()!=null) {
			jpa.setGarage(model.getGarage());
		}
		if(model.isGas()!=null) {
			jpa.setGarage(model.getGas());
		}
		if(model.isInternet()!=null) {
			jpa.setInternet(model.isInternet());
		}
		if(model.isPower()!=null) {
			jpa.setPower(model.isPower());
		}
		if(model.isWater()!=null) {
			jpa.setWater(model.isWater());
		}
		if(model.getSewer()!=null) {
			jpa.setSewer(model.getSewer());
		}
		
		if(RestPreconditions.checkString(model.getAddress1())) {
			jpa.setAddress1(model.getAddress1());
		}
		if(RestPreconditions.checkString(model.getAddress2())) {
			jpa.setAddress2(model.getAddress2());
		}
		if(RestPreconditions.checkString(model.getCity())) {
			jpa.setCity(model.getCity());
		}
		if(RestPreconditions.checkString(model.getCountry())) {
			jpa.setCountry(model.getCountry());
		}
		if(RestPreconditions.checkString(model.getDescription())) {
			jpa.setDescription(model.getDescription());
		}
		if(RestPreconditions.checkString(model.getTitle())) {
			jpa.setTitle(model.getTitle());
		}
		if(RestPreconditions.checkString(model.getCurrency())) {
			jpa.setCurrency(model.getCurrency());
		}
		if(model.getPrice()!=null) {
			jpa.setPrice(model.getPrice());
		}
		
		return jpa;
	}
	
	// find plot by id :
	public PlotDTO findOneById(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		return convertJpaToModel(RestPreconditions.checkNotNull(plotRepo.getOne(id),"Finf plot error","Plot with id = "+id+" doesn't exist"));
	}
	
	private void checkPostDataPresent(final PlotDTO model) throws MyRestPreconditionsException {
		MyRestPreconditionsException e = new MyRestPreconditionsException("Create new plot error",
				"some data are missing from the request");
		
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			e.getErrors().add("plot vertices set cannot be empty");
		}
		if(RestPreconditions.checkString(model.getTitle())) {
			e.getErrors().add("title is mandatory");
		}
		if(RestPreconditions.checkString(model.getDescription())) {
			e.getErrors().add("description is mandatory");
		}
		if(RestPreconditions.checkString(model.getAddress1())) {
			e.getErrors().add("Address1 is mandatory");
		}
		if(RestPreconditions.checkString(model.getCity())) {
			e.getErrors().add("city is mandatory");
		}
		if(RestPreconditions.checkString(model.getCountry())) {
			e.getErrors().add("country is mandatory");
		}
		
		if(!e.getErrors().isEmpty())
			throw e;
	}
	
	public PlotDTO addNew(PlotDTO model, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Create new plot error", "You cannot create new plot with an empty object in request.");
		model.setId(null); // just in case
		
		checkPostDataPresent(model);
		
		//TODO: Check plot is convex :
		
		//TODO: check that it doesn't overlap with other plots in db :
		
		//check how many plots the user is currently selling :
		UserJPA ujpa = userRepo.findOneByUsername(username);
		if(RestPreconditions.checkNotNull(ujpa, "Create new plot error",
				"User with username "+username+" doesn't exist.").getPlots().size()<=MAX_NUM_PLOTS) {
			PlotJPA jpa = convertModelToJpa(model);
			jpa.setUserJpa(ujpa);
			ujpa.getPlots().add(jpa);
			
			return convertJpaToModel(plotRepo.save(jpa));
		} else {
			throw new MyRestPreconditionsException("Create new plot error","You have reached the maximum number of plots you can create.");
		}
	}
	
	private boolean checkPatchDataPresent(PlotDTO model) {
		return (model.getVertices()!=null && !model.getVertices().isEmpty()) ||
				
				RestPreconditions.checkString(model.getAddress1()) ||
				RestPreconditions.checkString(model.getAddress2()) ||
				RestPreconditions.checkString(model.getCity()) ||
				RestPreconditions.checkString(model.getCountry()) ||
				RestPreconditions.checkString(model.getCurrency()) ||
				RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getTitle()) ||
				
				model.getSewer()!=null ||
				model.isGarage()!=null ||
				model.isGas()!=null ||
				model.isInternet()!=null ||
				model.isPower()!=null ||
				model.isWater()!=null ||
				model.getPrice()!=null
				;
	}
	
	public PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Edit plot error", "You cannot edit plot with an empty object in request.");
		RestPreconditions.checkId(id);
		
		model.setId(id);
		if(checkPatchDataPresent(model)) {
			// check plot exists
			PlotJPA jpa = RestPreconditions.checkNotNull(plotRepo.getOne(id), 
					"Edit plot error", "You are trying to edit a plot that doesn't exist in our database");
			// check user is editing his plot and not someone else's
			RestPreconditions.assertTrue(jpa.getUserJpa().getUsername().equals(username), 
					"Edit plot error", "You are trying to edit someone else's plot");
			
			//TODO: Check plot is convex :
			
			//TODO: check that it doesn't overlap with other plots in db :
			
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
		RestPreconditions.assertTrue(jpa.getUserJpa().getUsername().equals(username), 
				"Delete plot error", "You are trying to delete someone else's plot");
		
		plotRepo.delete(jpa);
	}
}