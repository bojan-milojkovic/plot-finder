package com.plot.finder.plot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.plot.finder.user.entity.UserJPA;

@Entity
@Table(name = "plot")
public class PlotJPA {
	
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
	
	@Column
	private String polygon;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserJPA userJpa;
	
	public PlotJPA() {
		setLl_x(900f);
		setLl_y(900f);
		setUr_x(-900f);
		setUr_y(-900f);
	}

	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column
	private String address1;
	
	@Column
	private String address2;
	
	@Column
	private String city;
	
	@Column
	private String country;
	
	@Column
	private boolean power;
	
	@Column
	private boolean water;
	
	@Column
	private boolean gas;
	
	@Column
	private boolean sewer;
	
	@Column
	private boolean internet;
	
	@Column
	private boolean garage;
	
	@Column
	private Integer price;
	
	@Column
	private Integer size;
	
	@Column
	private String currency;


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

	public UserJPA getUserJpa() {
		return userJpa;
	}

	public void setUserJpa(UserJPA userJpa) {
		this.userJpa = userJpa;
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

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public boolean isWater() {
		return water;
	}

	public void setWater(boolean water) {
		this.water = water;
	}

	public boolean isGas() {
		return gas;
	}

	public void setGas(boolean gas) {
		this.gas = gas;
	}

	public boolean isSewer() {
		return sewer;
	}

	public void setSewer(boolean sewer) {
		this.sewer = sewer;
	}

	public boolean isInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}

	public boolean isGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
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
}