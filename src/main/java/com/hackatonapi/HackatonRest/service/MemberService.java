package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Member;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public interface MemberService {
    MemberDTO findMemberById(Long id);
    MemberDTO addMember(MemberDTO memberDTO);
    MainSkillDTO setMainSkill(String memberName, String skillName);
    Set<MemberDTO> findMembersWithRequiredSkills(List<RequiredSkillDTO> requiredSkillDTOS);
    boolean isValidHeistMember(MemberDTO memberDTO,
                               List<RequiredSkillDTO> requiredSkillDTOS,
                               ZonedDateTime heistStart,
                               ZonedDateTime heistEnd);

}
