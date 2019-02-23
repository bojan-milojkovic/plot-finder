package com.plot.finder.watched.entity;

import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
public class WatchedDTO {
	@JsonProperty(access = Access.READ_WRITE)
	@Min(0)
	private Long id;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ll_x;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ll_y;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ur_x;
	
	@JsonProperty(access = Access.READ_WRITE)
	private Float ur_y;

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
}