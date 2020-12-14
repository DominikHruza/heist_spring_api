package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.service.HeistService;
import com.hackatonapi.HackatonRest.service.MemberService;
import com.hackatonapi.HackatonRest.service.RequiredSkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class HeistFacadeImpl implements HeistFacade {
    HeistService heistService;
    HeistRepository heistRepository;
    RequiredSkillsService requiredSkillsService;
    MemberService memberService;

    @Autowired
    public HeistFacadeImpl(HeistService heistService,
                           RequiredSkillsService requiredSkillsService,
                           HeistRepository heistRepository,
                           MemberService memberService
                           ) {
        this.heistService = heistService;
        this.requiredSkillsService = requiredSkillsService;
        this.heistRepository = heistRepository;
        this.memberService = memberService;
    }

    @Override
    public HeistDTO makeNewHeist(HeistDTO heistDTO) {
        HeistDTO savedHeist = heistService.saveNewHeist(heistDTO);
        List<RequiredSkillDTO> requiredSkillDTOS =
                requiredSkillsService.bulkAddRequiredSkills(
                        savedHeist.getName(),
                        heistDTO.getSkills()
                );

        savedHeist.setSkills(requiredSkillDTOS);
        return savedHeist;
    }

    @Override
    public void updateRequiredSkills(Long heistId, UpdateRequiredSkillsDTO requiredSkillDTOs) {
       Optional<Heist> heistOpt = heistRepository.findById(heistId);

       if(!heistOpt.isPresent()){
           throw new ResourceNotFoundException("Heist with id " + heistId + " does not exist.");
       }
       requiredSkillsService.bulkAddRequiredSkills(heistOpt.get().getName(), requiredSkillDTOs.getSkills());
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long heistId) {
        if(!heistRepository.findById(heistId).isPresent()){
            throw new ResourceNotFoundException("Heist with id " + heistId + " does not exist.");
        }

        List<RequiredSkillDTO> requiredSkillDTOS =
                requiredSkillsService.findRequiredSkillsOfHeist(heistId);

        Set<MemberDTO> eligibleMembers =
                memberService.findMembersWithRequiredSkills(requiredSkillDTOS);

        return new EligibleMembersDTO(
                requiredSkillDTOS,
                eligibleMembers
        );
    }
}
