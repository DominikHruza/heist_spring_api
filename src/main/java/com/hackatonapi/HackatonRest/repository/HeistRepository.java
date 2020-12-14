package com.hackatonapi.HackatonRest.repository;

import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeistRepository extends JpaRepository<Heist, Long>{
    Optional<Heist> findByName(String name);

}
