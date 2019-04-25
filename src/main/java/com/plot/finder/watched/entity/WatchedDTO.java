package com.plot.finder.watched.entity;

import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
public class WatchedDTO implements WatchedParent {
	@JsonProperty(access = Access.READ_WRITE)
	@Min(0)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ll_lng;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ll_lat;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ur_lng;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ur_lat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getLl_lng() {
		return ll_lng;
	}

	public void setLl_lng(final Float ll_lng) {
		this.ll_lng = ll_lng;
	}

	public Float getLl_lat() {
		return ll_lat;
	}

	public void setLl_lat(final Float ll_lat) {
		this.ll_lat = ll_lat;
	}

	public Float getUr_lng() {
		return ur_lng;
	}

	public void setUr_lng(final Float ur_lng) {
		this.ur_lng = ur_lng;
	}

	public Float getUr_lat() {
		return ur_lat;
	}

	public void setUr_lat(final Float ur_lat) {
		this.ur_lat = ur_lat;
	}
}