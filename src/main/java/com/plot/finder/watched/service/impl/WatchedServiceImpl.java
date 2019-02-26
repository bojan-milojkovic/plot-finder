package com.plot.finder.watched.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotJPA;
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
	
	public WatchedDTO addEdit(WatchedDTO model, final String username) throws MyRestPreconditionsException{
		RestPreconditions.checkNotNull(model, "Edit plot error", "You cannot edit plot with an empty object in request.");
		
		String title = "Find plots by coordinates error";
		RestPreconditions.assertTrue(Math.abs(model.getLl_x()) < 90, 
				title, "(LL) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(model.getUr_x()) < 90, 
				title, "(UR) Latitude is outside the [-90,+90] range.");
		RestPreconditions.assertTrue(Math.abs(model.getLl_y()) < 180, 
				title, "(LL) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(Math.abs(model.getUr_y()) < 180, 
				title, "(UR) Longitude is outside the [-180,+180] range.");
		RestPreconditions.assertTrue(model.getLl_x()!=model.getUr_x() && model.getLl_y()!=model.getUr_y(), 
				title, "You did not enter a valid set of coordinates.");
		
		UserJPA entity = userRepo.findOneByUsername(username);
		
		return convertJpaToModel(watchedRepo.save(saveCoordinates(entity!=null ? entity.getWatched() : new WatchedJPA(), model)));
	}
	
	private WatchedJPA saveCoordinates(WatchedJPA entity, WatchedDTO model){
		
		// just in case
		if(model.getLl_x() > model.getUr_x()){
			Float tmp = model.getLl_x();
			model.setLl_x(model.getUr_x());
			model.setUr_x(tmp);
		}
		if(model.getLl_y() > model.getUr_y()){
			Float tmp = model.getLl_y();
			model.setLl_y(model.getUr_y());
			model.setUr_y(tmp);
		}
		
		return (WatchedJPA) setCoordinates(entity, model);
	}
	
	private WatchedParent setCoordinates(WatchedParent entity, WatchedParent model){
		entity.setLl_x(model.getLl_x());
		entity.setLl_y(model.getLl_y());
		entity.setUr_x(model.getUr_x());
		entity.setUr_y(model.getUr_y());
		
		return entity;
	}
	
	public void deleteArea(final String username) throws MyRestPreconditionsException {
		watchedRepo.delete(RestPreconditions.checkNotNull(userRepo.findOneByUsername(username).getWatched(),
				   "Delete watched area error","User "+ username +" doesn't have a watched area."));
	}
	
	public void checkNewPlotIsInsideAnArea(final PlotJPA entity){
		
		watchedRepo.findAreasWatchingPlot(entity.getLl_x(), entity.getLl_y(), entity.getUr_x(), entity.getUr_y())
			.stream()
			.filter(j -> j.getUserJpa().getId() != entity.getUserJpa().getId())
			.forEach(j->{
				String subject = "Plotfinder notification";
				String body = "A new plot has been added in the area you are watching.\n\rLogin to your account to view it.\n\r";
				emailUtil.sendEmail(j.getUserJpa().getEmail(), subject, body);
			});
	}
}