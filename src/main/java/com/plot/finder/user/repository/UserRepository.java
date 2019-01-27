package com.plot.finder.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.plot.finder.user.entity.UserJPA;

public interface UserRepository extends JpaRepository<UserJPA, Long>{

}
