package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.*;
import com.hackatonapi.HackatonRest.exception.InvalidHeistStatusException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.service.HeistService;
import com.hackatonapi.HackatonRest.service.MemberService;
import com.hackatonapi.HackatonRest.service.RequiredSkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class HeistFacadeImpl implements HeistFacade {
    HeistService heistService;
    RequiredSkillsService requiredSkillsService;
    MemberService memberService;
    MemberMapper memberMapper;

    @Autowired
    public HeistFacadeImpl(HeistService heistService,
                           RequiredSkillsService requiredSkillsService,
                           MemberService memberService,
                           MemberMapper memberMapper
                           ) {
        this.heistService = heistService;
        this.requiredSkillsService = requiredSkillsService;
        this.memberService = memberService;
        this.memberMapper = memberMapper;
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
       HeistDTO heistDTO = heistService.getHeist(heistId);
        if(heistDTO.getStatus().equals(HeistStatus.IN_PROGRESS) ||
                heistDTO.getStatus().equals(HeistStatus.READY)){
           throw new InvalidHeistStatusException(
                   "Cannot change required skills at this heist stage.");
       }
       requiredSkillsService.bulkAddRequiredSkills(heistDTO.getName(), requiredSkillDTOs.getSkills());
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long heistId) {
        HeistDTO heistDTO = heistService.getHeist(heistId);
        HeistStatus status = heistDTO.getStatus();
        if(status.equals(HeistStatus.READY) ||
                status.equals(HeistStatus.IN_PROGRESS)){
            throw new InvalidHeistStatusException(
                    "Heist is in status " + status + " cannot show members"
            );
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

    @Override
    @Transactional
    public void confirmParticipants(Long heistId, ParticipantsDTO participants) {
        HeistDTO heistDTO = heistService.getHeist(heistId);

        if(!heistDTO.getStatus().equals(HeistStatus.PLANNING)){
            throw new InvalidHeistStatusException(
                    "Heist with id " + heistId + " is not in status: " + HeistStatus.PLANNING
            );
        }

        //Find required skills for given heist
        List<RequiredSkillDTO> requiredSkillsOfHeist =
                requiredSkillsService.findRequiredSkillsOfHeist(heistId);

        //Validate all participants in participants array
        for (String memberName : participants.getMembers()) {
            //Find member if exists
            MemberDTO memberDTO = memberService.findMember(memberName);
            //Check if member is eligible to participate in heist
            memberService.isValidHeistMember(
                    memberDTO,
                    requiredSkillsOfHeist,
                    heistDTO.getStartTime(),
                    heistDTO.getEndTime());

            //if member passes validation save to heist
            heistService.addMemberToHeist(heistId, memberName);
        }
        //if validation does not throw errors
        //and members are saved change status
        heistService.changeStatus(heistId, HeistStatus.READY);
    }

    @Override
    public OutcomeDTO getHeistOutcome(Long id) {
        Double membersPercentage = heistService.calculateMembersPercentage(id);
        switch (membersPercentage){
            case 0 < 0.5:
                heistService.setHeistOutcome(id, HeistOutcome.FAILED);
                memberService.bulkChangeStatus(heistService.getHeistMembers(id), 1.0, null);
            case 0.5 < 0.75:
            case 0.75 < 1:
            case 1:
        }
    }
}
