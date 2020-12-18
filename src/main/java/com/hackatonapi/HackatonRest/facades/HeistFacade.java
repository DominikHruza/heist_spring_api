package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import org.springframework.stereotype.Component;


public interface HeistFacade {
   HeistDTO makeNewHeist(HeistDTO heistDTO);
   void updateRequiredSkills(Long heistId, UpdateRequiredSkillsDTO requiredSkillDTOs);
   EligibleMembersDTO getEligibleMembers(Long heistId);
   void confirmParticipants(Long heistId, ParticipantsDTO participants);
   OutcomeDTO getHeistOutcome(Long id);
}
