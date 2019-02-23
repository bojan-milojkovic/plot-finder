package com.plot.finder.plot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "flags")
public class Flags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="flag_id")
	private Long id;
	
	@Column
	private String flag;
	
	@ManyToOne
	@JoinColumn(name="plot_id")
	private PlotJPA plotJpa;

	public Flags() {
		super();
	}

	public Flags(String flag, PlotJPA plotJpa) {
		super();
		this.flag = flag;
		this.plotJpa = plotJpa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public PlotJPA getPlotJpa() {
		return plotJpa;
	}

	public void setPlotJpa(PlotJPA plotJpa) {
		this.plotJpa = plotJpa;
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o != null && o instanceof Flags) {
			if(o==this) {
        		return true;
        	}
			return this.flag.equals(((Flags)o).getFlag()) && 
					this.plotJpa.getId() == ((Flags)o).getPlotJpa().getId();
		}
		
		return false;
	}
	
	@Override
    public int hashCode() {
		long h = 1125899906842597L; // prime
		
		for (int i = 0; i < flag.length(); i++) {
			h = 31*h + flag.charAt(i);
		}
		
		return (int)(31*h + plotJpa.getId().hashCode());
    }
}