package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.EligibleMembersDTO;
import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateRequiredSkillsDTO;

public interface HeistFacade {
   HeistDTO makeNewHeist(HeistDTO heistDTO);
   void updateRequiredSkills(Long heistId, UpdateRequiredSkillsDTO requiredSkillDTOs);
   EligibleMembersDTO getEligibleMembers(Long heistId);
}
