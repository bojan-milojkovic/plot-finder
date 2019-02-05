package com.plot.finder.plot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plot.finder.plot.entities.PlotJPA;

@Repository
public interface PlotRepository extends JpaRepository<PlotJPA, Long>{

}
