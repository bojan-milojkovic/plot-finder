package com.plot.finder.plot.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.images.storage.StorageService;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.Vertice;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.plot.repository.PlotCriteriaRepository;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;
import com.plot.finder.watched.entity.WatchedJPA;
import com.plot.finder.watched.service.WatchedService;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.PolygonArea;

@Service
public class PlotServiceImpl implements PlotService {

	private PlotRepository plotRepo;
	private UserRepository userRepo;
	private StorageService storageServiceImpl;
	private PlotCriteriaRepository plotCriteriaRepo;
	private WatchedService watchedServiceImpl;
	
	private static final Integer MAX_NUM_PLOTS = 3;
	
	@Autowired
	public PlotServiceImpl(PlotRepository plotRepo, UserRepository userRepo, StorageService storageServiceImpl, PlotCriteriaRepository plotCriteriaRepo, WatchedService watchedServiceImpl) {
		this.plotRepo = plotRepo;
		this.userRepo = userRepo;
		this.storageServiceImpl = storageServiceImpl;
		this.plotCriteriaRepo = plotCriteriaRepo;
		this.watchedServiceImpl = watchedServiceImpl;
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
		model.setDistrict(jpa.getDistrict());
		model.setCity(jpa.getCity());
		model.setCountry(jpa.getCountry());
		model.setDescription(jpa.getDescription());
		model.setTitle(jpa.getTitle());
		model.setSize(jpa.convertM2ToSizeUnit());
		model.setSizeUnit(jpa.getSizeUnit());
		model.setPrice(jpa.getPrice());
		model.setCurrency(jpa.getCurrency());
		model.setAdded(jpa.getAdded());
		model.setExpires(jpa.getExpires());
		
		model.setGarage(jpa.containsFlag("garage"));
		model.setGas(jpa.containsFlag("gas"));
		model.setInternet(jpa.containsFlag("internet"));
		model.setPower(jpa.containsFlag("power"));
		model.setSewer(jpa.containsFlag("sewer"));
		model.setWater(jpa.containsFlag("water"));
		model.setHouse(jpa.containsFlag("house"));
		model.setFarming(jpa.containsFlag("farming"));
		model.setGrazing(jpa.containsFlag("grazing"));
		model.setOrchard(jpa.containsFlag("orchard"));
		
		if(jpa.containsFlag("sale")){
			model.setType("SALE");
		} else if(jpa.containsFlag("rent")){
			model.setType("RENT");
		}
		
		return model;
	}

	private PlotJPA setCoordinates(List<Vertice> vertices, PlotJPA jpa) {
		String tmp="";
		// jpa ll and ur x/y coordinates are already set
		for(Vertice v : vertices) {
			if(v.getLat() < jpa.getWa().getLl_y()) {
				jpa.getWa().setLl_y(v.getLat());
			}
			if(v.getLat() > jpa.getWa().getUr_y()) {
				jpa.getWa().setUr_y(v.getLat());
			}
			if(v.getLng() < jpa.getWa().getLl_x()) {
				jpa.getWa().setLl_x(v.getLng());
			}
			if(v.getLng() > jpa.getWa().getUr_x()) {
				jpa.getWa().setUr_x(v.getLng());
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
		
		return convertJpaListToModelList(plotCriteriaRepo.getPlotByCoordinates(
											ll.getLng()<ur.getLng() ? ll.getLng() : ur.getLng(), 
											ll.getLat()<ur.getLat() ? ll.getLat() : ur.getLat(),
													
											ll.getLng()>ur.getLng() ? ll.getLng() : ur.getLng(), 
											ll.getLat()>ur.getLat() ? ll.getLat() : ur.getLat()   ));
	}
	
	public List<PlotDTO> findPlotsByProperties(final PlotDTO model) throws MyRestPreconditionsException{
		RestPreconditions.checkNotNull(model, "Find plots by properties error", "You must specify the request body.");
		RestPreconditions.assertTrue(checkPatchDataPresent(model) 
				|| model.getMaxPrice()!=null || model.getMaxSize()!=null 
				|| model.getMinPrice()!=null || model.getMinSize()!=null, "Find plots by properties error", "You must provide some search parameters");
		return convertJpaListToModelList(plotCriteriaRepo.getPlotByProperties(model));
	}
	
	private List<PlotDTO> convertJpaListToModelList(List<PlotJPA> input){
		return input.stream()
				.filter(j -> j.getUserJpa().isNotLocked())
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
	
	public void renewPlotAdd(final Long id, final String username) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		
		PlotJPA jpa = RestPreconditions.checkNotNull(plotRepo.getOne(id), "Renew plot error", "The plot you are renewing does not exist");
		
		RestPreconditions.assertTrue(jpa.getUserJpa().getUsername().equals(username), "Renew plot error", "You are trying to renew the add for someone else's plot");
		
		if(!jpa.getExpires().isBefore(LocalDate.now())) {
			jpa.setExpires(jpa.getExpires().plusDays(30));
			
			plotRepo.save(jpa);
		} else {
			this.delete(id, username, false);
			throw new MyRestPreconditionsException("Your plot has just expired","Please post the plot add again");
		}
	}
	
	private PlotJPA convertModelToJpa(final PlotDTO model) {
		PlotJPA jpa = null;
		
		if(model.getId()==null) {
			jpa = new PlotJPA();
			jpa.setAdded(LocalDate.now());
			jpa.setExpires(LocalDate.now().plusDays(30));
			// for add, we save flags later
		} else {
			jpa = plotRepo.getOne(model.getId());
			jpa = saveFlags(jpa, model);
		}
		
		if(model.getVertices()!=null && !model.getVertices().isEmpty()) {
			jpa = setCoordinates(model.getVertices(), jpa);
		}
		if(RestPreconditions.checkString(model.getDistrict())) {
			jpa.setDistrict(model.getDistrict());
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
			jpa.setSize(model.convertSizeToM2(model.getSize()));
		}
		if(RestPreconditions.checkString(model.getSizeUnit())) {
			jpa.setSizeUnit(model.getSizeUnit());
		}
		
		return jpa;
	}
	
	// find plot by id :
	public PlotDTO findOneById(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		PlotJPA jpa = RestPreconditions.checkNotNull(plotRepo.getOne(id),"Finf plot error","Plot with id = "+id+" doesn't exist");
		
		return jpa.getUserJpa().isNotLocked() ? convertJpaToModel(jpa) : null;
	}
	
	private void checkPostDataPresent(final PlotDTO model) throws MyRestPreconditionsException {
		MyRestPreconditionsException e = new MyRestPreconditionsException("Create new plot error",
				"some data are invalid or missing from the request");
		
		
		if(model.getVertices().size()<4 || model.getVertices().size()>8) {
			e.getErrors().add("plot must have between 4 and 8 vertices");
		} else if(!isConvex(model.getVertices())) {
			e.getErrors().add("The plot you are entering is not a convex polygon.");
		} else if (checkPlotArea(model)) {
			e.getErrors().add("The inputed area does not match with the area calculated from vertices.");
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
		if(!RestPreconditions.checkString(model.getType())) {
			e.getErrors().add("Type is mandatory (sale or rent)");
		}
		if(!(RestPreconditions.checkString(model.getCity()) || RestPreconditions.checkString(model.getDistrict()))) {
			e.getErrors().add("you must enter ether city or district");
		}
		if(!RestPreconditions.checkString(model.getCountry())) {
			e.getErrors().add("country is mandatory");
		}
		if(model.getSize()==null) {
			e.getErrors().add("plot size is mandatory");
		}
		if(!RestPreconditions.checkString(model.getSizeUnit())) {
			e.getErrors().add("unit of size is mandatory");
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
	
	private PlotJPA saveFlags(PlotJPA jpa, PlotDTO model){
		jpa.addRemoveFlag("garage", model.isGarage());
		jpa.addRemoveFlag("gas", model.isGas());
		jpa.addRemoveFlag("internet", model.isInternet());
		jpa.addRemoveFlag("power", model.isPower());
		jpa.addRemoveFlag("water", model.isWater());
		jpa.addRemoveFlag("sewer", model.getSewer());
		jpa.addRemoveFlag("house", model.getHouse());
		jpa.addRemoveFlag("farming", model.getFarming());
		jpa.addRemoveFlag("grazing", model.getGrazing());
		jpa.addRemoveFlag("orchard", model.getOrchard());
		
		if(RestPreconditions.checkString(model.getType())) {
			jpa.addRemoveFlag("rent", false);
			jpa.addRemoveFlag("sale", false);
			jpa.addRemoveFlag(model.getType().toLowerCase(), true);
		}
		
		return jpa;
	}
	
	private void checkFiles(PlotDTO model) throws MyRestPreconditionsException {
		storageServiceImpl.checkFile(model.getFile1());
		storageServiceImpl.checkFile(model.getFile2());
		storageServiceImpl.checkFile(model.getFile3());
		storageServiceImpl.checkFile(model.getFile4());
	}
	
	private boolean checkPlotArea(final PlotDTO model) {
		PolygonArea p = new PolygonArea(Geodesic.WGS84, true);
		
		model.getVertices().stream().forEach(v -> p.AddPoint(v.getLat(), v.getLng()));
		
		Double vArea = p.Compute().area;
		Integer iArea = model.convertSizeToM2(model.getSize());
		
		return (vArea > iArea/2 && vArea < iArea*1.5);
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
									.getPlots().size()<MAX_NUM_PLOTS, 
									"Create new plot error","You have reached the maximum number of plots you can create.");
		
		PlotJPA jpa = convertModelToJpa(model);
		jpa.setUserJpa(ujpa);
		ujpa.getPlots().add(jpa);
		
		saveAll(jpa, model);
		
		watchedServiceImpl.checkNewPlotIsInsideAnArea(jpa); 
		
		return convertJpaToModel(jpa);
	}
	
	public List<PlotDTO> findPlotsInUserWatchedArea(final String username) throws MyRestPreconditionsException{
		// username exists if it passed security
		
		WatchedJPA jpa = RestPreconditions.checkNotNull(userRepo.findOneByUsername(username).getWatched(),
				"Find plots by watched area error","User "+username+" did not specify a watched area");
		
		return findPlotsByCoordinates(new Vertice(jpa.getLl_x(), jpa.getLl_y()), new Vertice(jpa.getUr_x(), jpa.getUr_y()));
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
				RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getTitle()) ||
				RestPreconditions.checkString(model.getDistrict()) ||
				RestPreconditions.checkString(model.getType()) ||
				RestPreconditions.checkString(model.getCurrency()) ||
				RestPreconditions.checkString(model.getSizeUnit()) ||
				
				model.getPrice()!=null ||
				model.getSize()!=null ||
				
				model.getHouse()!=null ||
				model.isPower()!=null ||
				model.isWater()!=null ||
				model.getSewer()!=null ||
				
				model.isGarage()!=null ||
				model.isGas()!=null ||
				model.isInternet()!=null ||
				
				model.getFarming()!=null ||
				model.getGrazing()!=null ||
				model.getOrchard()!=null ||
				
				model.getFile1()!=null
				;
	}
	
	@Transactional
	private PlotJPA saveAll(PlotJPA jpa, PlotDTO model) throws MyRestPreconditionsException{
		saveModelFiles(model, 
				(plotRepo.save(
							saveFlags(jpa.getId()==null ? plotRepo.save(jpa) : jpa, model) // so jpa would have id for flags to use in hashCode
										)).getId());
		
		return jpa;
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
		if(!(model.getVertices()==null || model.getVertices().isEmpty())) {
			RestPreconditions.assertTrue(model.getVertices().size()>3 && model.getVertices().size()<9,
					"Edit plot error","Number of vertices in plot must be between 4 and 8");
			RestPreconditions.assertTrue(isConvex(model.getVertices()), 
					"Edit plot error","Plot polygon you are entering is not convex.");
			RestPreconditions.assertTrue(checkPlotArea(model), 
					"Edit plot error","Inputed area doesn't match with area calculated from vertices.");
		}
		// check images :
		checkFiles(model);
		
		model.setId(id);
		
		RestPreconditions.assertTrue(checkPatchDataPresent(model), 
				"Edit plot error","You must provide some editable data");
			
		RestPreconditions.assertTrue(
				// check plot exists
				(RestPreconditions.checkNotNull(plotRepo.getOne(id), "Edit plot error", 
												"You are trying to edit a plot that doesn't exist in our database")) // returns PlotJPA
									// check user is editing his plot and not someone else's
									.getUserJpa().getUsername().equals(username), 
									"Edit plot error", "You are trying to edit someone else's plot");
		
		//TODO: check that it doesn't overlap with other plots in db :
		
		// save images :
		saveModelFiles(model, id);
		
		return convertJpaToModel(saveAll(convertModelToJpa(model), model));
	}
	
	@Transactional
	public void delete(final Long id, final String username, final boolean checks) throws MyRestPreconditionsException {
		
		if(checks){
			RestPreconditions.checkId(id);
			
			RestPreconditions.assertTrue(
					// check plot exists :
					(RestPreconditions.checkNotNull(plotRepo.getOne(id), 
													"Delete plot error", "Plot with id = "+id+" doesn't exist")) // returns PlotJPA
										// check user is editing his plot and not someone else's
										.getUserJpa().getUsername().equals(username), 
										"Delete plot error", "You are trying to delete someone else's plot");
		}
		
		plotRepo.deleteById(id);
		
		storageServiceImpl.deleteImage(id, "File1");
		storageServiceImpl.deleteImage(id, "File2");
		storageServiceImpl.deleteImage(id, "File3");
		storageServiceImpl.deleteImage(id, "File4");
	}
}