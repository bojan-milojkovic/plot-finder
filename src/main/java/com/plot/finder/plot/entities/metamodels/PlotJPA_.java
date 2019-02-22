package com.plot.finder.plot.entities.metamodels;

import com.plot.finder.plot.entities.Flags;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.user.entity.UserJPA;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PlotJPA.class)
public abstract class PlotJPA_ {

	public static volatile SingularAttribute<PlotJPA, Float> ur_x;
	public static volatile SingularAttribute<PlotJPA, Float> ur_y;
	public static volatile SingularAttribute<PlotJPA, String> country;
	public static volatile SingularAttribute<PlotJPA, String> address2;
	public static volatile SingularAttribute<PlotJPA, String> city;
	public static volatile SingularAttribute<PlotJPA, LocalDateTime> added;
	public static volatile SingularAttribute<PlotJPA, String> address1;
	public static volatile SingularAttribute<PlotJPA, Float> ll_y;
	public static volatile SetAttribute<PlotJPA, Flags> flags;
	public static volatile SingularAttribute<PlotJPA, String> description;
	public static volatile SingularAttribute<PlotJPA, UserJPA> userJpa;
	public static volatile SingularAttribute<PlotJPA, Float> ll_x;
	public static volatile SingularAttribute<PlotJPA, String> title;
	public static volatile SingularAttribute<PlotJPA, String> polygon;
	public static volatile SingularAttribute<PlotJPA, Integer> size;
	public static volatile SingularAttribute<PlotJPA, Integer> price;
	public static volatile SingularAttribute<PlotJPA, String> district;
	public static volatile SingularAttribute<PlotJPA, String> currency;
	public static volatile SingularAttribute<PlotJPA, Long> id;

}

