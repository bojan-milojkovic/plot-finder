package com.plot.finder.plot.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.entities.PlotDTO;
import com.plot.finder.plot.entities.metamodels.PlotJPA;
import com.plot.finder.plot.entities.metamodels.PlotJPA_;
import com.plot.finder.util.RestPreconditions;
import com.plot.finder.watched.entity.metamodel.WatchedJPA;
import com.plot.finder.watched.entity.metamodel.WatchedJPA_;

@Repository
public class PlotCriteriaRepository {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public List<PlotJPA> getPlotByCoordinates(final float ll_x, final float ll_y, final float ur_x, final float ur_y){
		CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
		
		CriteriaQuery<WatchedJPA> cquerry = builder.createQuery(WatchedJPA.class);
		Root<WatchedJPA> croot = cquerry.from(WatchedJPA.class);
		
		cquerry.select(croot);
		
		Predicate dPred = builder.disjunction();
		
		// plot's ll_x is between arguments ll_x and ur_x  :
		dPred = builder.or(dPred, builder.and(
										builder.greaterThanOrEqualTo(croot.get(WatchedJPA_.ll_x), ll_x), 
						   				builder.lessThanOrEqualTo(croot.get(WatchedJPA_.ll_x), ur_x)));
		
		// plot's ur_x is between arguments ll_x and ur_x  :
		dPred = builder.or(dPred, builder.and(
										builder.greaterThanOrEqualTo(croot.get(WatchedJPA_.ur_x), ll_x),
										builder.lessThanOrEqualTo(croot.get(WatchedJPA_.ur_x), ur_x)));
		
		// plot's ll_y is between arguments ll_y and ur_y  :
		dPred = builder.or(dPred, builder.and(
										builder.greaterThanOrEqualTo(croot.get(WatchedJPA_.ll_y), ll_y),
										builder.lessThanOrEqualTo(croot.get(WatchedJPA_.ll_y), ur_y)));
		
		// plot's ur_y is between arguments ll_y and ur_y  :
		dPred = builder.or(dPred, builder.and(
										builder.greaterThanOrEqualTo(croot.get(WatchedJPA_.ur_y), ll_y),
										builder.lessThanOrEqualTo(croot.get(WatchedJPA_.ur_y), ur_y)));
		
		// inner join to PlotJPA for order by :
		javax.persistence.criteria.Join<WatchedJPA, PlotJPA> cjoin = croot.join(WatchedJPA_.plotJpa);
		
		List<WatchedJPA> wList = entityManagerFactory.createEntityManager()
				.createQuery(cquerry.where(builder.and(dPred, croot.get(WatchedJPA_.plotJpa).isNotNull()))
						.orderBy(builder.desc(cjoin.get(PlotJPA_.added))))
				.setFirstResult(0)
				.setMaxResults(40)
				.getResultList();
		 
		if(wList!=null && !wList.isEmpty())	
			return wList.stream()
				.map(w -> w.getPlotJpa())
				//.filter(p -> p!=null)
				.collect(Collectors.toList());
		
		return new ArrayList<PlotJPA>();
	}
	
	public List<PlotJPA> getPlotByProperties(final PlotDTO model) throws MyRestPreconditionsException {
		CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
		
		CriteriaQuery<PlotJPA> cquerry = builder.createQuery(PlotJPA.class);
		
		Root<PlotJPA> croot = cquerry.from(PlotJPA.class);
		
		cquerry.select(croot);
		
		Predicate pred = builder.isNotNull(croot.<String>get(PlotJPA_.flags));
		
		if(RestPreconditions.checkString(model.getCity())) {
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.city), model.getCity()));
		}
		if(RestPreconditions.checkString(model.getPhone())) {
			RestPreconditions.verifyStringFormat(model.getPhone(), "^([+][0-9]{1,3})?[0-9 -]+$", 
					"User create error", "mobile number is in invalid format");
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.phone), model.getPhone()));
		}
		if(RestPreconditions.checkString(model.getCountry())) {
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.country), model.getCountry()));
		}
		if(RestPreconditions.checkString(model.getDistrict())) {
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.district), model.getDistrict()));
		}
		if(RestPreconditions.checkString(model.getAddress1())) {
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.address1), model.getAddress1()));
		}
		if(RestPreconditions.checkString(model.getAddress2())) {
			pred = builder.and(pred, builder.equal(croot.get(PlotJPA_.address2), model.getAddress2()));
		}
		
		if(model.getMaxPrice()!=null) {
			pred = builder.and(pred, builder.lessThanOrEqualTo(croot.get(PlotJPA_.price), model.getMaxPrice()));
		}
		if(model.getMinPrice()!=null) {
			pred = builder.and(pred, builder.greaterThanOrEqualTo(croot.get(PlotJPA_.price), model.getMinPrice()));
		}
		
		if(model.getMaxSize()!=null) {
			pred = builder.and(pred, builder.lessThanOrEqualTo(croot.get(PlotJPA_.size), model.convertSizeToM2(model.getMaxSize())));
		}
		if(model.getMinSize()!=null) {
			pred = builder.and(pred, builder.greaterThanOrEqualTo(croot.get(PlotJPA_.size), model.convertSizeToM2(model.getMinSize())));
		}
		
		String bword = model.flagsToBWord();
		if(!bword.matches("[01]0{12}")){
			char sr = bword.toCharArray()[0];
			pred = builder.and(pred, builder.like(croot.get(PlotJPA_.flags), 
					bword.replaceAll("0", "_").replaceFirst("[01]", ""+sr)));
		}
		
		return entityManagerFactory.createEntityManager()
				.createQuery(cquerry.where(pred).orderBy(builder.desc(croot.get(PlotJPA_.added))))
				.setFirstResult(0)
				.setMaxResults(40)
				.getResultList();
	}
}