package com.plot.finder.watched.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.plot.finder.watched.entity.WatchedJPA;

@Repository
public interface WatchedRepository extends JpaRepository<WatchedJPA, Long>{

	// check at least one plot vertice is inside the watched area
	@Query("SELECT w FROM WatchedJPA w where (w.ll_x<=?1 AND w_ur_x>=?1 AND w.ll_y<=?2 AND AND w.ur_y>=?2) OR "+ // (ll_x, ll_y) in area
											"(w.ll_x<=?1 AND w_ur_x>=?1 AND w.ll_y<=?4 AND AND w.ur_y>=?4) OR "+ // (ll_x, ur_y) in area
											"(w.ll_x<=?3 AND w_ur_x>=?3 AND w.ll_y<=?2 AND AND w.ur_y>=?2) OR "+ // (ur_x, ll_y) in area
											"(w.ll_x<=?3 AND w_ur_x>=?3 AND w.ll_y<=?4 AND AND w.ur_y>=?4)")     // (ur_x, ur_y) in area
	List<WatchedJPA> findAreasWatchingPlot(float ll_x, float ll_y, float ur_x, float ur_y);
}
