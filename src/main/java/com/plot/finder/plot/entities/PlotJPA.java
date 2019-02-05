package com.plot.finder.plot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class PlotJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@Column(name="low_left_x")
	private Float ll_x;
	
	@Column(name="low_left_y")
	private Float ll_y;
	
	@Column(name="up_right_x")
	private Float ur_x;

	@Column(name="up_right_y")
	private Float ur_y;
	
	@Column
	private String polygon;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getLl_x() {
		return ll_x;
	}

	public void setLl_x(Float ll_x) {
		this.ll_x = ll_x;
	}

	public Float getLl_y() {
		return ll_y;
	}

	public void setLl_y(Float ll_y) {
		this.ll_y = ll_y;
	}

	public Float getUr_x() {
		return ur_x;
	}

	public void setUr_x(Float ur_x) {
		this.ur_x = ur_x;
	}

	public Float getUr_y() {
		return ur_y;
	}

	public void setUr_y(Float ur_y) {
		this.ur_y = ur_y;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}
}