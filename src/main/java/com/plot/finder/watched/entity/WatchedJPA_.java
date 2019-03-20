package com.plot.finder.watched.entity;

import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.watched.entity.WatchedJPA;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WatchedJPA.class)
public abstract class WatchedJPA_ {

	public static volatile SingularAttribute<WatchedJPA, Float> ur_x;
	public static volatile SingularAttribute<WatchedJPA, Float> ur_y;
	public static volatile SingularAttribute<WatchedJPA, Float> ll_y;
	public static volatile SingularAttribute<WatchedJPA, UserJPA> userJpa;
	public static volatile SingularAttribute<WatchedJPA, Long> id;
	public static volatile SingularAttribute<WatchedJPA, Float> ll_x;
	public static volatile SingularAttribute<WatchedJPA, PlotJPA> plotJpa;

}

