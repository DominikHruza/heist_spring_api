package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Heist;

import java.util.List;

public interface MemberSkillLevelService {
    MemberSkillsInfoDTO getMemberSkills(Long memberId);
    List<MemberSkillDTO> bulkSaveMemberSkillLevel(List<MemberSkillDTO> memberSkillDTOS, String memberName);
    MemberSkillDTO saveMemberSkillLevel(MemberSkillDTO memberSkillDTO, String memberName);
    void deleteMemberSkillLevel(Long memberId, String skillName);
    void LevelUpSkills(String memberName, HeistDTO heistDTO);
}
