package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;

import java.util.List;
import java.util.Set;

public interface MemberService {
    MemberDTO addMember(MemberDTO memberDTO);
    MainSkillDTO setMainSkill(String memberName, String skillName);
    Set<MemberDTO> findMembersWithRequiredSkills(List<RequiredSkillDTO> requiredSkillDTOS);
}
