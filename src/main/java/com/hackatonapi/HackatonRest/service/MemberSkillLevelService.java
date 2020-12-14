package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.MemberSkillDTO;
import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateMemberSkillsDTO;

import java.util.List;

public interface MemberSkillLevelService {
    List<MemberSkillDTO> bulkSaveMemberSkillLevel(List<MemberSkillDTO> memberSkillDTOS, String memberName);
    MemberSkillDTO saveMemberSkillLevel(MemberSkillDTO memberSkillDTO, String memberName);
    void deleteMemberSkillLevel(Long memberId, String skillName);
}
