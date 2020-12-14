package com.hackatonapi.HackatonRest.repository;

import com.hackatonapi.HackatonRest.entity.MemberSkillLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSkillLevelRepository extends JpaRepository<MemberSkillLevel, Long> {
    Optional<MemberSkillLevel> findBySkillNameAndMemberId(String name, Long memberId);
}
