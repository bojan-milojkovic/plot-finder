package com.plot.finder.user.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
public class UserDTO {
	
	private Long id;
	
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String mobile;
	
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime registerred;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public LocalDateTime getRegisterred() {
		return registerred;
	}

	public void setRegisterred(LocalDateTime registerred) {
		this.registerred = registerred;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}