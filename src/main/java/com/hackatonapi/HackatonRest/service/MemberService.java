package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.MemberStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public interface MemberService {
    MemberDTO findMember(Long id);
    MemberDTO findMember(String name);
    MemberDTO addMember(MemberDTO memberDTO);
    MainSkillDTO setMainSkill(String memberName, String skillName);
    Set<MemberDTO> findMembersWithRequiredSkills(List<RequiredSkillDTO> requiredSkillDTOS);
    boolean isValidHeistMember(MemberDTO memberDTO,
                               List<RequiredSkillDTO> requiredSkillDTOS,
                               ZonedDateTime heistStart,
                               ZonedDateTime heistEnd);
    void bulkChangeStatus(List<CurrentHeistMemberDTO> members, Double percentage, MemberStatus status);
}
