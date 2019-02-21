package com.plot.finder.plot.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(Include.NON_NULL)
public class PlotDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(access = Access.READ_WRITE)
	@Min(0)
	private Long id;
	
	@JsonProperty("vertices")
	private List<Vertice> vertices = new ArrayList<Vertice>();
	
	private MultipartFile file1;
	
	private MultipartFile file2;
	
	private MultipartFile file3;
	
	private MultipartFile file4;
	
	
	@JsonProperty(access = Access.READ_WRITE)
	private String title;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String description;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String address1;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String address2;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String city;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String country;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Pattern(regexp="[A-Z]{3}")
	private String currency;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean power;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean water;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean gas;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean sewer;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean internet;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Boolean garage;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Integer price;
	
	@JsonProperty(access = Access.READ_WRITE)
	@Min(1)
	private Integer size;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer minSize;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer maxSize;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer minPrice;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(1)
	private Integer maxPrice;

	public PlotDTO() {
		super();
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

	//@JsonDeserialize(using = VerticeListDeserializer.class)
	public void setVertices(List<Vertice> vertices) {
		this.vertices = vertices;
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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public MultipartFile getFile1() {
		return file1;
	}

	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}

	public MultipartFile getFile2() {
		return file2;
	}

	public void setFile2(MultipartFile file2) {
		this.file2 = file2;
	}

	public MultipartFile getFile3() {
		return file3;
	}

	public void setFile3(MultipartFile file3) {
		this.file3 = file3;
	}

	public MultipartFile getFile4() {
		return file4;
	}

	public void setFile4(MultipartFile file4) {
		this.file4 = file4;
	}

	public Integer getMinSize() {
		return minSize;
	}

	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o != null && o instanceof PlotJPA) {
			if(o==this) {
        		return true;
        	}
			return this.id == ((PlotJPA)o).getId();
		}
		
		return false;
	}
	
	@Override
    public int hashCode() {
		return id.hashCode();
	}
}