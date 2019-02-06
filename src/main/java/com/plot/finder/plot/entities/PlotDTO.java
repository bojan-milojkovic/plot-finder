package com.plot.finder.plot.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PlotDTO {
	
	@Min(0)
	private Long id;
	
	private List<Vertice> vertices;
	
	private String title;
	
	private String description;
	
	private String address1;
	
	private String address2;
	
	private String city;
	
	private String country;
	
	@Min(1)
	private Integer price;
	
	private String currency;
	
	private Boolean power;
	
	private Boolean water;
	
	private Boolean gas;
	
	private Boolean sewer;
	
	private Boolean internet;
	
	private Boolean garage;
	
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
		
		private static final Pattern ptn = Pattern.compile("[+-]?[0-9]{1,3}[.][0-9]+");
		
		public Vertice(){
			
		}
		public Vertice(String v) {
			Matcher m = ptn.matcher(v);
			
			BigDecimal bd = new BigDecimal(m.group(0));
			lat = bd.setScale(6, BigDecimal.ROUND_HALF_EVEN).floatValue();
			
			bd = new BigDecimal(m.group(1));
			lng = bd.setScale(6, BigDecimal.ROUND_HALF_EVEN).floatValue();
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

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public Boolean isPower() {
		return power;
	}

	public void setPower(Boolean power) {
		this.power = power;
	}

	public Boolean isWater() {
		return water;
	}

	public void setWater(Boolean water) {
		this.water = water;
	}

	public Boolean isGas() {
		return gas;
	}

	public void setGas(Boolean gas) {
		this.gas = gas;
	}

	public Boolean getSewer() {
		return sewer;
	}

	public void setSewer(Boolean sewer) {
		this.sewer = sewer;
	}

	public Boolean getPower() {
		return power;
	}

	public Boolean getWater() {
		return water;
	}

	public Boolean getGas() {
		return gas;
	}

	public Boolean getInternet() {
		return internet;
	}

	public Boolean getGarage() {
		return garage;
	}

	public Boolean isInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
	}

	public Boolean isGarage() {
		return garage;
	}

	public void setGarage(Boolean garage) {
		this.garage = garage;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}