package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.MemberDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateMemberSkillsDTO;

public interface MemberFacade {
    MemberDTO addNewMember(MemberDTO memberDTO);
    void updateSkillsAndMainSkill(UpdateMemberSkillsDTO skillsData, Long memberId);
}
