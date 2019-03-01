package com.plot.finder.plot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.plot.finder.plot.entities.PlotJPA;

@Repository
public interface PlotRepository extends JpaRepository<PlotJPA, Long>, JpaSpecificationExecutor<PlotJPA>{

	@Query("SELECT p FROM PlotJPA p where p.expires BETWEEN curdate() and curdate() + interval 3 day")
	List<PlotJPA> findAboutToExpire();
	
	@Query("SELECT p FROM PlotJPA p where p.expires < curdate()")
	List<PlotJPA> findExpired();
}
