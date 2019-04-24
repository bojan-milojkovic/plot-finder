package com.plot.finder.user.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.plot.finder.plot.entities.PlotJPA;
import com.plot.finder.security.entities.RoleJPA;
import com.plot.finder.security.entities.UserHasRolesJPA;
import com.plot.finder.watched.entity.WatchedJPA;

@Entity
@Table(name = "user")
public class UserJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column
	private String email;
	
	@Column
	private String phone1;
	
	@Column
	private String phone2;
	
	@Column
	private Boolean active;

	@Column(name="not_locked")
	private Boolean notLocked;
	
	@Column
	private String identifier;
	
	@Column(name="reg_date")
	private LocalDateTime registration;
	
	@Column(name="last_login")
	private LocalDateTime lastLogin;

	@Column(name="last_password_change")
	private LocalDateTime lastPasswordChange;
	
	@Column(name="last_update")
	private LocalDateTime lastUpdate;
	
	@OneToMany(mappedBy="userJpa", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserHasRolesJPA> userHasRolesJpa = new HashSet<UserHasRolesJPA>();
	
	public boolean checkIfUserHasRole(final Long roleId) {
		UserHasRolesJPA uhr = new UserHasRolesJPA();
		uhr.setUserSecurityJpa(this);
		RoleJPA role = new RoleJPA();
		role.setRoleId(roleId);
		uhr.setRoleJpa(role);
		
		return userHasRolesJpa.contains(uhr);
	}
	
	@OneToMany(mappedBy="userJpa", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlotJPA> plots = new HashSet<PlotJPA>();

	@OneToOne(mappedBy="userJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private WatchedJPA watched;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getRegistration() {
		return registration;
	}

	public void setRegistration(LocalDateTime registration) {
		this.registration = registration;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean isNotLocked() {
		return notLocked;
	}

	public void setNotLocked(Boolean notLocked) {
		this.notLocked = notLocked;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public LocalDateTime getLastPasswordChange() {
		return lastPasswordChange;
	}

	public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
		this.lastPasswordChange = lastPasswordChange;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set<UserHasRolesJPA> getUserHasRolesJpa() {
		return userHasRolesJpa;
	}

	public void setUserHasRolesJpa(Set<UserHasRolesJPA> userHasRolesJpa) {
		this.userHasRolesJpa = userHasRolesJpa;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getNotLocked() {
		return notLocked;
	}

	public Set<PlotJPA> getPlots() {
		return plots;
	}

	public void setPlots(Set<PlotJPA> plots) {
		this.plots = plots;
	}

	public WatchedJPA getWatched() {
		return watched;
	}

	public void setWatched(WatchedJPA watched) {
		this.watched = watched;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}