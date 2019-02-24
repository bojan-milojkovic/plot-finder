package com.plot.finder.watched.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.plot.finder.watched.entity.WatchedJPA;

@Repository
public interface WatchedRepository extends JpaRepository<WatchedJPA, Long>{

	@Query("SELECT w FROM WatchedJPA w where (w.ll_x <= ?1 AND w.ur_x >= ?1) OR (w.ll_x <= ?3 AND w.ur_x >= ?3) OR "+
											"(w.ll_y <= ?2 AND w.ur_y >= ?2) OR (w.ll_y <= ?4 AND w.ur_y >= ?4)")
	List<WatchedJPA> findAreasWatchingPlot(float ll_x, float ll_y, float ur_x, float ur_y);
}
