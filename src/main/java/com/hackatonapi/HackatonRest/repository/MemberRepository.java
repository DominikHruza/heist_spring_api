package com.hackatonapi.HackatonRest.repository;

import com.hackatonapi.HackatonRest.DTO.MemberDTO;
import com.hackatonapi.HackatonRest.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    @Query("select m from Member m " +
            "join m.memberSkillLevels l " +
            "join l.skill s " +
            "where length(l.level) >= length(?2) and " +
            "s.name = ?1 and " +
            "m.status = 'AVAILABLE' or " +
            "m.status = 'RETIRED' "
    )
    List<Member> findBySkillNameAndLevel(String skillName, String level);
}
