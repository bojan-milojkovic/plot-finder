package com.plot.finder.plot.entities.metamodels;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import com.plot.finder.plot.entities.Flags;
import com.plot.finder.plot.entities.PlotJPA;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Flags.class)
public abstract class Flags_ {

	public static volatile SingularAttribute<Flags, String> flag;
	public static volatile SingularAttribute<Flags, Long> id;
	public static volatile SingularAttribute<Flags, PlotJPA> plotJpa;

}

