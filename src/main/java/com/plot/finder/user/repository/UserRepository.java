package com.plot.finder.user.repository;

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
	
	@Query("select u from UserJPA u where u.phone1 = :param or u.phone2 = :param")
	public UserJPA findOneByPhone(@Param("param") final String phone);
}
