package com.plot.finder.user.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.plot.finder.plot.entities.metamodels.PlotJPA;
import com.plot.finder.watched.entity.metamodel.WatchedJPA;

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
	
	@Column
	private String email;
	
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
	
	@Column
	private String authorities;
	
	public List<SimpleGrantedAuthority> getRoleList(){
		return Arrays.asList(authorities.split("#")).stream()
				.map(a -> new SimpleGrantedAuthority(a))
				.collect(Collectors.toList());
	}
	
	public boolean checkIfUserHasAdminRole() {
		return authorities.contains("ADMIN");
	}
	
	@OneToMany(mappedBy="userJpa", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlotJPA> plots = new HashSet<PlotJPA>();

	@OneToOne(mappedBy="userJpa", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private WatchedJPA watched;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	/*public Set<UserHasRolesJPA> getUserHasRolesJpa() {
		return userHasRolesJpa;
	}

	public void setUserHasRolesJpa(Set<UserHasRolesJPA> userHasRolesJpa) {
		this.userHasRolesJpa = userHasRolesJpa;
	}*/

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

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
}