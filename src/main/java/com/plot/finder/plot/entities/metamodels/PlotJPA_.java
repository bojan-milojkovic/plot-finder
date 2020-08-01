package com.plot.finder.plot.entities.metamodels;

import com.plot.finder.plot.entities.metamodels.PlotJPA;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.watched.entity.metamodel.WatchedJPA;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PlotJPA.class)
public abstract class PlotJPA_ {

	public static volatile SingularAttribute<PlotJPA, String> country;
	public static volatile SingularAttribute<PlotJPA, LocalDate> expires;
	public static volatile SingularAttribute<PlotJPA, String> address2;
	public static volatile SingularAttribute<PlotJPA, String> city;
	public static volatile SingularAttribute<PlotJPA, LocalDate> added;
	public static volatile SingularAttribute<PlotJPA, String> address1;
	public static volatile SingularAttribute<PlotJPA, String> description;
	public static volatile SingularAttribute<PlotJPA, UserJPA> userJpa;
	public static volatile SingularAttribute<PlotJPA, String> phone;
	public static volatile SingularAttribute<PlotJPA, WatchedJPA> wa;
	public static volatile SingularAttribute<PlotJPA, String> title;
	public static volatile SingularAttribute<PlotJPA, String> polygon;
	public static volatile SingularAttribute<PlotJPA, Integer> size;
	public static volatile SingularAttribute<PlotJPA, Integer> price;
	public static volatile SingularAttribute<PlotJPA, String> district;
	public static volatile SingularAttribute<PlotJPA, String> sizeUnit;
	public static volatile SingularAttribute<PlotJPA, String> flags;
	public static volatile SingularAttribute<PlotJPA, String> currency;
	public static volatile SingularAttribute<PlotJPA, Long> id;

}

