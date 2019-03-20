package com.plot.finder.plot.entities;

import java.math.BigDecimal;

public class Vertice {
	private Float lat;
	private Float lng;
	
	public Vertice(){
		super();
	}
	
	public Vertice(Float x, Float y) {
		this.lng = x;
		this.lat = y;
	}
	
	public static Vertice createFromString(String v) {
		String[] latLng = v.split("#");
		
		Vertice tmp = new Vertice();
		
		BigDecimal bd = new BigDecimal(latLng[0]);
		tmp.setLat(bd.setScale(6, BigDecimal.ROUND_HALF_EVEN).floatValue());
		
		bd = new BigDecimal(latLng[1]);
		tmp.setLng(bd.setScale(6, BigDecimal.ROUND_HALF_EVEN).floatValue());
		
		return tmp;
	}
	
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Float getLng() {
		return lng;
	}
	public void setLng(Float lng) {
		this.lng = lng;
	}
}
