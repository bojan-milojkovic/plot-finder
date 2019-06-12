package com.plot.finder.watched.entity.metamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.plot.finder.plot.entities.metamodels.PlotJPA;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.watched.entity.WatchedParent;

@Entity
@Table(name = "watched_area")
public class WatchedJPA implements WatchedParent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column
	private Float ll_x;
	
	@Column
	private Float ll_y;
	
	@Column
	private Float ur_x;

	@Column
	private Float ur_y;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserJPA userJpa;
	
	@OneToOne
	@JoinColumn(name = "plot_id")
	private PlotJPA plotJpa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getLl_lng() {
		return ll_x;
	}

	public void setLl_lng(final Float ll_lng) {
		this.ll_x = ll_lng;
	}

	public Float getLl_lat() {
		return ll_y;
	}

	public void setLl_lat(final Float ll_lat) {
		this.ll_y = ll_lat;
	}

	public Float getUr_lng() {
		return ur_x;
	}

	public void setUr_lng(final Float ur_lng) {
		this.ur_x = ur_lng;
	}

	public Float getUr_lat() {
		return ur_y;
	}

	public void setUr_lat(final Float ur_lat) {
		this.ur_y = ur_lat;
	}

	public UserJPA getUserJpa() {
		return userJpa;
	}

	public void setUserJpa(UserJPA userJpa) {
		this.userJpa = userJpa;
	}

	public PlotJPA getPlotJpa() {
		return plotJpa;
	}

	public void setPlotJpa(PlotJPA plotJpa) {
		this.plotJpa = plotJpa;
	}
}