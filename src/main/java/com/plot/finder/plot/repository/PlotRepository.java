package com.plot.finder.plot.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.plot.finder.plot.entities.PlotJPA;

@Repository
public interface PlotRepository extends JpaRepository<PlotJPA, Long>, JpaSpecificationExecutor<PlotJPA>{

	@Query("SELECT p FROM PlotJPA p where p.expires BETWEEN CURDATE() and ?1")
	List<PlotJPA> findAboutToExpire(LocalDate future);
	
	@Query("SELECT p FROM PlotJPA p where p.expires < curdate()")
	List<PlotJPA> findExpired();
}
