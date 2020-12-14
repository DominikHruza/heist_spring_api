package com.hackatonapi.HackatonRest.repository;

import com.hackatonapi.HackatonRest.entity.RequiredSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RequiredSkillRepository extends JpaRepository<RequiredSkill, Long> {
    @Transactional
    @Query("select rs from RequiredSkill rs " +
            "join rs.skill s " +
            "join rs.heist h " +
            "where s.name = ?2  and h.id = ?1 ")
    Optional<RequiredSkill> findByHeistAndSkill(Long heistId, String skillName);

    List<RequiredSkill> findByHeistId(Long heistId);
}
