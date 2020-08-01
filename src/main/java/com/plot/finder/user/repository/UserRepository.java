package com.plot.finder.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.plot.finder.user.entity.UserJPA;

@Repository
public interface UserRepository extends JpaRepository<UserJPA, Long>{

	@Query("select u from UserJPA u where u.username = :param")
	public UserJPA findOneByUsername(@Param("param") String username);
	
	@Query("select u from UserJPA u where u.email = :param")
	public UserJPA findOneByEmail(@Param("param") final String email);
	
	@Query("select u from UserJPA u where u.identifier = :param")
	public UserJPA findByIdentifier(@Param("param") final String key);
	
	@Query("select u from UserJPA u where u.notLocked = 0 AND u.lastUpdate < :param")
	public List<UserJPA> findLockedExpired(@Param("param") LocalDateTime past);
}
