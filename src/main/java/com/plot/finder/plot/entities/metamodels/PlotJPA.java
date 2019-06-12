package com.plot.finder.plot.entities.metamodels;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.plot.finder.user.entity.UserJPA;
import com.plot.finder.watched.entity.metamodel.WatchedJPA;

@Entity
@Table(name = "plot")
public class PlotJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column
	private String polygon;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserJPA userJpa;
	
	@OneToOne(mappedBy="plotJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private WatchedJPA wa;
	
	public PlotJPA() {
		wa = new WatchedJPA();
		wa.setLl_lng(900f);
		wa.setLl_lat(900f);
		wa.setUr_lng(-900f);
		wa.setUr_lat(-900f);
		wa.setPlotJpa(this);
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
	private String district;
	
	@Column
	private String city;
	
	@Column
	private String country;
	
	@Column
	private Integer price;
	
	@Column
	private Integer size;
	
	@Column
	private String currency;
	
	@Column(name="size_unit")
	private String sizeUnit;
	
	@Column
	private LocalDate added;
	
	@Column
	private LocalDate expires;
	
	@Column(name="flags")
	private String flags;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getAdded() {
		return added;
	}

	public void setAdded(LocalDate added) {
		this.added = added;
	}
	
	public LocalDate getExpires() {
		return expires;
	}

	public void setExpires(LocalDate expires) {
		this.expires = expires;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public WatchedJPA getWa() {
		return wa;
	}

	public void setWa(WatchedJPA wa) {
		this.wa = wa;
	}
	
	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public Integer convertM2ToSizeUnit() {
		switch(getSizeUnit()) {
			case "m2":
				return getSize();
			case "Ar":
				return (Integer)(getSize()/100);
			default:
				return (Integer)(getSize()/10000);
		}
	}
}