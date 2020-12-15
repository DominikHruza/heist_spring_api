package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.EligibleMembersDTO;
import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.ParticipantsDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateRequiredSkillsDTO;
import org.springframework.stereotype.Component;


public interface HeistFacade {
   HeistDTO makeNewHeist(HeistDTO heistDTO);
   void updateRequiredSkills(Long heistId, UpdateRequiredSkillsDTO requiredSkillDTOs);
   EligibleMembersDTO getEligibleMembers(Long heistId);
   void confirmParticipants(Long heistId, ParticipantsDTO participants);
}
