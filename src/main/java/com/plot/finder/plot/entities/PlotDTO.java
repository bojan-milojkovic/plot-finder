package com.plot.finder.plot.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PlotDTO {
	
	@Min(0)
	private Long id;
	
	@NotNull
	private List<Vertice> vertices;
	
	public PlotDTO() {
		vertices = new ArrayList<Vertice>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Vertice> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertice> vertices) {
		this.vertices = vertices;
	}
	
	public static class Vertice{
		private Float lat;
		private Float lng;
		
		private static final Pattern ptn = Pattern.compile("[0-9]{1,3}[.][0-9]+");
		
		public Vertice(){
			
		}
		public Vertice(String v) {
			Matcher m = ptn.matcher(v);
			
			BigDecimal bd = new BigDecimal(m.group(0));
			lat = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN).floatValue();
			
			bd = new BigDecimal(m.group(1));
			lng = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN).floatValue();
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
}
