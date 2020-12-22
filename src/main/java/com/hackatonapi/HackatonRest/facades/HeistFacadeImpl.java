package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.*;
import com.hackatonapi.HackatonRest.exception.InvalidHeistStatusException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.service.*;
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
    MailService mailService;
    MemberSkillLevelService memberSkillLevelService;

    @Autowired
    public HeistFacadeImpl(HeistService heistService,
                           RequiredSkillsService requiredSkillsService,
                           MemberService memberService,
                           MemberMapper memberMapper,
                           MailService mailService,
                           MemberSkillLevelService memberSkillLevelService
                           ) {
        this.heistService = heistService;
        this.requiredSkillsService = requiredSkillsService;
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.mailService = mailService;
        this.memberSkillLevelService = memberSkillLevelService;
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
            boolean validMember = memberService.isValidHeistMember(
                    memberDTO,
                    requiredSkillsOfHeist,
                    heistDTO.getStartTime(),
                    heistDTO.getEndTime());

            //if member passes validation save to heist
            heistService.addMemberToHeist(heistId, memberName);
            String msg = "You have been added to a new heist starting at " + heistDTO.getStartTime();
            mailService.sendMail(
                    memberDTO.getEmail(),
                    "Member of a new heist!",
                    msg
        );

        }
        //if validation does not throw errors
        //and members are saved change status
        heistService.changeStatus(heistId, HeistStatus.READY);

    }

    @Override
    public OutcomeDTO getHeistOutcome(Long id) {

        //Get members that participate in given heist
        List<CurrentHeistMemberDTO> heistMembers = heistService.getHeistMembers(id);

        //Calculate percentage of members in heist that fulfill heist criteria
        Double membersPercentage = heistService.calculateMembersPercentage(id);

        HeistOutcome outcome;
        if (membersPercentage < 0.5) {
            outcome = heistService.setHeistOutcome(id, HeistOutcome.FAILED);
            memberService.bulkChangeStatus(
                    heistMembers,
                    1.0, null);
        } else if (membersPercentage >= 0.5 && membersPercentage < 0.75) {
            outcome = heistService.setHeistOutcome(id);
            changeMemberStatusOutcomeBased(id, outcome);

        } else if (membersPercentage >= 0.75 && membersPercentage < 1) {
            outcome = heistService.setHeistOutcome(id, HeistOutcome.SUCCEEDED);
            memberService.bulkChangeStatus(
                    heistMembers,
                    0.33,
                    MemberStatus.INCARCERATED);
        } else {
            outcome = heistService.setHeistOutcome(id, HeistOutcome.SUCCEEDED);
        }

        HeistDTO heistDTO = heistService.getHeist(id);
        updateSkillsOfParticipants(heistMembers, heistDTO);
        return new OutcomeDTO(outcome.name());
    }

    private void changeMemberStatusOutcomeBased(Long heistId, HeistOutcome outcome){
        if (outcome.equals(HeistOutcome.FAILED)){
            memberService.bulkChangeStatus(
                    heistService.getHeistMembers(heistId),
                    0.66,
                    null
            );
        } else if (outcome.equals(HeistOutcome.SUCCEEDED)){
            memberService.bulkChangeStatus(
                    heistService.getHeistMembers(heistId),
                    0.33,
                    null
            );
        }
    }

    private void updateSkillsOfParticipants(List<CurrentHeistMemberDTO> heistMembers,
                                           HeistDTO heistDTO){

        for (var heistMember:
             heistMembers) {
            memberSkillLevelService.LevelUpSkills(heistMember.getName(), heistDTO);
        }
    }
}
