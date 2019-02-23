package com.plot.finder.watched.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plot.finder.watched.entity.WatchedJPA;

@Repository
public interface WatchedRepository extends JpaRepository<WatchedJPA, Long>{

}
