package com.plot.finder.watched.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.plot.entities.Vertice;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.user.repository.UserRepository;
import com.plot.finder.util.RestPreconditions;
import com.plot.finder.watched.entity.WatchedDTO;
import com.plot.finder.watched.entity.WatchedJPA;
import com.plot.finder.watched.entity.WatchedParent;
import com.plot.finder.watched.repository.WatchedRepository;
import com.plot.finder.watched.service.WatchedService;

@Service
public class WatchedServiceImpl implements WatchedService {
	
	private WatchedRepository watchedRepo;
	private UserRepository userRepo;
	private EmailUtil emailUtil;
	
	@Autowired
	public WatchedServiceImpl(WatchedRepository watchedRepo, UserRepository userRepo, EmailUtil emailUtil) {
		this.watchedRepo = watchedRepo;
		this.userRepo = userRepo;
		this.emailUtil = emailUtil;
	}
	
	public WatchedDTO getWatched(final String username) throws MyRestPreconditionsException {
		return convertJpaToModel(RestPreconditions.checkNotNull(userRepo.findOneByUsername(username).getWatched(),
				   "Find watched area error","User "+ username +" did not specify an area to watch yet."));
	}
	
	private WatchedDTO convertJpaToModel(WatchedJPA entity){
		return (WatchedDTO) setCoordinates(new WatchedDTO(), entity);
	}
	
	public void checkRectangleCoordinates(final Float ll_lng, 
										  final Float ll_lat,
										  final Float ur_lng,
										  final Float ur_lat, final String title) throws MyRestPreconditionsException {
		
		RestPreconditions.assertTrue(Math.abs(ll_lat) < 90, 
				title, "(LL) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(ur_lat) < 90, 
				title, "(UR) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(ll_lng) < 180, 
				title, "(LL) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(Math.abs(ur_lng) < 180, 
				title, "(UR) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(ll_lat!=ur_lat && ll_lng!=ur_lng, 
				title, "You did not enter a valid set of coordinates.");
	}
	
	public WatchedDTO addEdit(WatchedDTO model, final String username) throws MyRestPreconditionsException{
		RestPreconditions.checkNotNull(model, "Watched area error", "You cannot add/edit watched area with an empty object in request.");
		
		checkRectangleCoordinates(model.getLl_lng(), model.getLl_lat(), model.getUr_lng(), model.getUr_lat(), "Watched area error");
		
		UserJPA entity = userRepo.findOneByUsername(username);
		
		return convertJpaToModel(watchedRepo.save(saveCoordinates(entity!=null ? entity.getWatched() : new WatchedJPA(), model)));
	}
	
	private WatchedJPA saveCoordinates(WatchedJPA entity, WatchedDTO model){
		
		// just in case
		if(model.getLl_lng() > model.getUr_lng()){
			Float tmp = model.getLl_lng();
			model.setLl_lng(model.getUr_lng());
			model.setUr_lng(tmp);
		}
		if(model.getLl_lat() > model.getUr_lat()){
			Float tmp = model.getLl_lat();
			model.setLl_lat(model.getUr_lat());
			model.setUr_lat(tmp);
		}
		
		return (WatchedJPA) setCoordinates(entity, model);
	}
	
	public Vertice watchedAreaCenter(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkId(id);
		WatchedJPA entity = RestPreconditions.checkNotNull(watchedRepo.getOne(id),
				"Watched area center error","Area with id="+id+" does not exist");
		
		return new Vertice( (entity.getLl_lng()+entity.getUr_lng())/2, (entity.getLl_lat()+entity.getUr_lat())/2 );
	}
	
	private WatchedParent setCoordinates(WatchedParent entity, WatchedParent model){
		entity.setLl_lng(model.getLl_lng());
		entity.setLl_lat(model.getLl_lat());
		entity.setUr_lng(model.getUr_lng());
		entity.setUr_lat(model.getUr_lat());
		
		return entity;
	}
	
	public void deleteArea(final String username) throws MyRestPreconditionsException {
		watchedRepo.delete(RestPreconditions.checkNotNull(userRepo.findOneByUsername(username).getWatched(),
				   "Delete watched area error","User "+ username +" doesn't have a watched area."));
	}
	
	public void checkNewPlotIsInsideAnArea(final PlotJPA entity){
		
		watchedRepo.findAreasWatchingPlot(entity.getWa().getLl_lng(), entity.getWa().getLl_lat(), entity.getWa().getUr_lng(), entity.getWa().getUr_lat())
			.stream()
			.filter(j -> j.getUserJpa().getId() != entity.getUserJpa().getId())
			.forEach(j->emailUtil.sendNewPlotEmail(entity));
	}
}