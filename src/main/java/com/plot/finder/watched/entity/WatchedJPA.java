package com.plot.finder.watched.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.plot.finder.user.entity.UserJPA;

@Entity
@Table(name = "watched_area")
public class WatchedJPA {

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
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserJPA userJpa;

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

	public UserJPA getUserJpa() {
		return userJpa;
	}

	public void setUserJpa(UserJPA userJpa) {
		this.userJpa = userJpa;
	}
}
