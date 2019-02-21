package com.plot.finder.plot.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.images.storage.StorageService;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.Vertice;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.plot.repository.CriteriaPlotRepository;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;

@Service
public class PlotServiceImpl implements PlotService {

	private PlotRepository plotRepo;
	private UserRepository userRepo;
	private StorageService storageServiceImpl;
	private CriteriaPlotRepository criteriaPlotRepository;
	
	private static final Integer MAX_NUM_PLOTS = 3;
	
	@Autowired
	public PlotServiceImpl(PlotRepository plotRepo, UserRepository userRepo, StorageService storageServiceImpl, CriteriaPlotRepository criteriaPlotRepository) {
		this.plotRepo = plotRepo;
		this.userRepo = userRepo;
		this.storageServiceImpl = storageServiceImpl;
		this.criteriaPlotRepository = criteriaPlotRepository;
	}
	
	// convert model to jpa :
	private PlotDTO convertJpaToModel(final PlotJPA jpa) throws MyRestPreconditionsException {
		PlotDTO model = new PlotDTO();
		
		model.setId(jpa.getId());
		
		model.setVertices(Arrays.asList(jpa.getPolygon().split("@"))
				.stream()
				.filter(v -> !((String)v).isEmpty())
				.map(v -> Vertice.createFromString((String) v))
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
		model.setSize(jpa.getSize());
		
		return model;
	}

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
	
	public List<PlotDTO> findPlotsByCoordinates(final Vertice ll, final Vertice ur) throws MyRestPreconditionsException{
		String title = "Find plots by coordinates error";
		RestPreconditions.assertTrue(Math.abs(ll.getLat()) < 90, 
				title, "(LL) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(ur.getLat()) < 90, 
				title, "(UR) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(ll.getLng()) < 180, 
				title, "(LL) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(Math.abs(ur.getLng()) < 180, 
				title, "(UR) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(ll.getLat()!=ur.getLat() && ll.getLng()!=ur.getLng(), 
				title, "You did not enter a valid set of coordinates.");
		
		return convertJpaListToModelList(criteriaPlotRepository.getPlotByCoordinates(
											ll.getLng()<ur.getLng() ? ll.getLng() : ur.getLng(), 
											ll.getLat()<ur.getLat() ? ll.getLat() : ur.getLat(),
													
											ll.getLng()>ur.getLng() ? ll.getLng() : ur.getLng(), 
											ll.getLat()>ur.getLat() ? ll.getLat() : ur.getLat()   ));
	}
	
	public List<PlotDTO> findPlotsByProperties(final PlotDTO model) throws MyRestPreconditionsException{
		return convertJpaListToModelList(criteriaPlotRepository.getPlotByProperties(model));
	}
	
	public Set<PlotDTO> filterPlotsByProperties(final Set<PlotDTO> list, final PlotDTO model) throws MyRestPreconditionsException {
		Set<PlotDTO> result = list;
		
		result = result.stream()
					   .filter(i -> i.getGarage()==(model.getGarage()==null?false:model.getGarage()))
					   .collect(Collectors.toSet());
		result = result.stream()
				   .filter(i -> i.getPower()==(model.getPower()==null?false:model.getPower()))
				   .collect(Collectors.toSet());
		result = result.stream()
				   .filter(i -> i.getWater()==(model.getWater()==null?false:model.getWater()))
				   .collect(Collectors.toSet());
		result = result.stream()
				   .filter(i -> i.getGas()==(model.getGas()==null?false:model.getGas()))
				   .collect(Collectors.toSet());
		result = result.stream()
				   .filter(i -> i.getSewer()==(model.getSewer()==null?false:model.getSewer()))
				   .collect(Collectors.toSet());
		result = result.stream()
				   .filter(i -> i.getInternet()==(model.getInternet()==null?false:model.getInternet()))
				   .collect(Collectors.toSet());
		
		// with each 'if' result shrinks
		if(RestPreconditions.checkString(model.getCountry())) {
			result = result.stream()
				.filter(i -> i.getCountry().equals(model.getCountry()))
				.collect(Collectors.toSet());
		}
		if(RestPreconditions.checkString(model.getCity())) {
			result = result.stream()
				.filter(i -> i.getCity().equals(model.getCity()))
				.collect(Collectors.toSet());
		}
		if(model.getMaxPrice()!=null) {
			result = result.stream()
				.filter(i -> i.getPrice()<model.getMaxPrice())
				.collect(Collectors.toSet());
		}
		if(model.getMinPrice()!=null) {
			result = result.stream()
				.filter(i -> i.getPrice()>model.getMinPrice())
				.collect(Collectors.toSet());
		}
		if(model.getMaxSize()!=null) {
			result = result.stream()
				.filter(i -> i.getCity().equals(model.getCity()))
				.collect(Collectors.toSet());
		}
		if(model.getMinSize()!=null) {
			result = result.stream()
				.filter(i -> i.getCity().equals(model.getCity()))
				.collect(Collectors.toSet());
		}
		return result;
	}
	
	private List<PlotDTO> convertJpaListToModelList(List<PlotJPA> input){
		return input.stream()
				.map(j -> {
					try {
						return convertJpaToModel(j);
					} catch (MyRestPreconditionsException e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	public ResponseEntity<Resource> getImage(Long id, String name, boolean isThumbnail, HttpServletRequest request) throws MyRestPreconditionsException{
		return storageServiceImpl.getImage(id, name, isThumbnail, request);
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
		if(model.getSize()!=null) {
			jpa.setSize(model.getSize());
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
		
		if(model.getVertices()!=null) {
			if(model.getVertices().size()<4 || model.getVertices().size()>8) {
				e.getErrors().add("plot vertices set must have between 4 and 8 vertices");
			} else if(!isConvex(model.getVertices())) {
				e.getErrors().add("The plot you are entering is not a convex polygon.");
			}
		}
		if(!RestPreconditions.checkString(model.getTitle())) {
			e.getErrors().add("title is mandatory");
		}
		if(!RestPreconditions.checkString(model.getDescription())) {
			e.getErrors().add("description is mandatory");
		}
		if(!RestPreconditions.checkString(model.getAddress1())) {
			e.getErrors().add("Address1 is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCity())) {
			e.getErrors().add("city is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCountry())) {
			e.getErrors().add("country is mandatory");
		}
		if(model.getSize()==null) {
			e.getErrors().add("plot size is mandatory");
		}
		if(model.getPrice()==null) {
			e.getErrors().add("price is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCurrency())) {
			e.getErrors().add("price currency is mandatory");
		}
		
		if(!e.getErrors().isEmpty()){
			throw e;
		}
	}
	
	private void checkFiles(PlotDTO model) throws MyRestPreconditionsException {
		storageServiceImpl.checkFile(model.getFile1());
		storageServiceImpl.checkFile(model.getFile2());
		storageServiceImpl.checkFile(model.getFile3());
		storageServiceImpl.checkFile(model.getFile4());
	}
	
	public PlotDTO addNew(PlotDTO model, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Create new plot error", "You cannot create new plot with an empty object in request.");
		model.setId(null); // just in case
		
		checkPostDataPresent(model);
		checkFiles(model);
		
		//TODO: check that it doesn't overlap with other plots in db :
		
		//check how many plots the user is currently selling :
		UserJPA ujpa = userRepo.findOneByUsername(username);
		RestPreconditions.assertTrue(
				(RestPreconditions.checkNotNull(ujpa, 
												"Create new plot error",
												"User with username "+username+" doesn't exist.")) // this returns UserJPA
									.getPlots().size()<=MAX_NUM_PLOTS, 
									"Create new plot error","You have reached the maximum number of plots you can create.");
		
		PlotJPA jpa = convertModelToJpa(model);
		jpa.setUserJpa(ujpa);
		ujpa.getPlots().add(jpa);
		
		jpa = plotRepo.save(jpa);
		// save images :
		saveModelFiles(model, jpa.getId());
		
		return convertJpaToModel(jpa);
	}
	
	private boolean isConvex(List<Vertice> _vertices) {
	    boolean sign = false;
	    int n = _vertices.size();

	    for(int i = 0; i < n; i++)
	    {
	        double dx1 = _vertices.get((i + 2) % n).getLng() - _vertices.get((i + 1) % n).getLng();
	        double dy1 = _vertices.get((i + 2) % n).getLat() - _vertices.get((i + 1) % n).getLat();
	        double dx2 = _vertices.get(i).getLng() - _vertices.get((i + 1) % n).getLng();
	        double dy2 = _vertices.get(i).getLat() - _vertices.get((i + 1) % n).getLat();
	        double zcrossproduct = dx1 * dy2 - dy1 * dx2;

	        if (i == 0)
	            sign = zcrossproduct > 0;
	        else if (sign != (zcrossproduct > 0))
	            return false;
	    }

	    return true;
	}
	
	private boolean checkPatchDataPresent(PlotDTO model) {
		return RestPreconditions.checkString(model.getAddress1()) ||
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
				model.getPrice()!=null ||
				model.getSize()!=null ||
				model.getFile1()!=null
				;
	}
	
	private void saveModelFiles(final PlotDTO model, final Long id) throws MyRestPreconditionsException {
		if(model.getFile1()!=null) {
			storageServiceImpl.saveImage(model.getFile1(), "File1", id);
			if(model.getFile2()!=null){
				storageServiceImpl.saveImage(model.getFile2(), "File2", id);
			}
			if(model.getFile3()!=null){
				storageServiceImpl.saveImage(model.getFile3(), "File3", id);
			}
			if(model.getFile4()!=null){
				storageServiceImpl.saveImage(model.getFile4(), "File4", id);
			}
		}
	}
	
	public PlotDTO edit(PlotDTO model, final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Edit plot error", "You cannot edit plot with an empty object in request.");// can happen
		RestPreconditions.checkId(id);
		
		// check vertices are convex and the number of vertices
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			if(!(model.getVertices().size()>3 && model.getVertices().size()<9)) {
				throw new MyRestPreconditionsException("Edit plot error","Number of vertices in plot must be between 4 and 8");
			} else if(!isConvex(model.getVertices())) {
				throw new MyRestPreconditionsException("Edit plot error","Plot polygon you are entering is not convex.");
			}
		}
		// check images :
		checkFiles(model);
		
		model.setId(id);
		if(checkPatchDataPresent(model)) {
			
			// check plot exists
			RestPreconditions.assertTrue(
					(RestPreconditions.checkNotNull(plotRepo.getOne(id), "Edit plot error", 
													"You are trying to edit a plot that doesn't exist in our database")) // returns PlotJPA
										// check user is editing his plot and not someone else's
										.getUserJpa().getUsername().equals(username), 
										"Edit plot error", "You are trying to edit someone else's plot");
			
			//TODO: check that it doesn't overlap with other plots in db :
			
			// save images :
			saveModelFiles(model, id);
			
			return convertJpaToModel(plotRepo.save(convertModelToJpa(model)));
		} else {
			throw new MyRestPreconditionsException("Edit plot error","You must provide some editable data");
		}
	}
	
	public void delete(final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		RestPreconditions.assertTrue(
				// check plot exists :
				(RestPreconditions.checkNotNull(plotRepo.getOne(id), 
												"Delete plot error", "Plot with id = "+id+" doesn't exist")) // returns PlotJPA
									// check user is editing his plot and not someone else's
									.getUserJpa().getUsername().equals(username), 
									"Delete plot error", "You are trying to delete someone else's plot");
		
		plotRepo.deleteById(id);
	}
}