package com.plot.finder.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plot.finder.security.entities.RoleJPA;

@Repository
public interface RoleRepository extends JpaRepository<RoleJPA, Long>{

}