package com.plot.finder.user.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
public class UserDTO {
	
	@Min(0)
	private Long id;
	
	@Pattern(regexp="^[A-Za-z0-9._-]{5,}$", message="Username can consist only of letters, digits, dot, dash and underscore")
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="Password cannot contain a white space or ; and must be at least 6 characters long")
	private String password;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp="^[^ ;]{6,}$", message="New password cannot contain a white space or ; and must be at least 6 characters long")
	private String newPassword;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	@Pattern(regexp="^[0-9 +-]+$", message="Phone number must consist of digits, blank spaces, + and -")
	private String phone1;
	
	@Pattern(regexp="^[0-9 +-]+$", message="Secong phone number must consist of digits, blank spaces, + and -")
	private String phone2;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape=Shape.STRING, pattern = "yyyy-MM-dd") // initialize LocalDate object from json string
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
	
	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}