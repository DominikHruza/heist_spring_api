package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.entity.HeistStatus;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.MemberStatus;
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
    HeistRepository heistRepository;
    RequiredSkillsService requiredSkillsService;
    MemberService memberService;
    MemberRepository memberRepository;
    MemberMapper memberMapper;

    @Autowired
    public HeistFacadeImpl(HeistService heistService,
                           RequiredSkillsService requiredSkillsService,
                           HeistRepository heistRepository,
                           MemberService memberService,
                           MemberRepository memberRepository,
                           MemberMapper memberMapper
                           ) {
        this.heistService = heistService;
        this.requiredSkillsService = requiredSkillsService;
        this.heistRepository = heistRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
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
       Optional<Heist> heistOpt = heistRepository.findById(heistId);

       if(!heistOpt.isPresent()){
           throw new ResourceNotFoundException("Heist with id " + heistId + " does not exist.");
       }

        Heist heist = heistOpt.get();
        if(heist.getStatus().equals(HeistStatus.IN_PROGRESS) ||
               heist.getStatus().equals(HeistStatus.READY)){
           throw new InvalidHeistStatusException(
                   "Cannot change required skills at this heist stage.");
       }
       requiredSkillsService.bulkAddRequiredSkills(heist.getName(), requiredSkillDTOs.getSkills());
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException("Heist with id " + heistId + " does not exist.");
        }

        HeistStatus heistStatus = heistOptional.get().getStatus();
        if(heistStatus.equals(HeistStatus.READY) ||
                heistStatus.equals(HeistStatus.IN_PROGRESS)){
            throw new InvalidHeistStatusException(
                    "Heist is in status " + heistStatus + " cannot show members"
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
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        //Check if heist exists
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException("Heist with id " + heistId + " does not exist");
        }

        //Check if heist is in planning status
        Heist heist = heistOptional.get();
        if(!heist.getStatus().equals(HeistStatus.PLANNING)){
            throw new InvalidHeistStatusException(
                    "Heist with id " + heistId + " is not in status: " + HeistStatus.PLANNING
            );
        }

        //Find required skills for given heist
        List<RequiredSkillDTO> requiredSkillsOfHeist =
                requiredSkillsService.findRequiredSkillsOfHeist(heistId);

        //Validate all participants in participants array
        for (String memberName : participants.getMembers()) {
            //Check if member exists
            Optional<Member> memberOptional = memberRepository.findByName(memberName);
            if (!memberOptional.isPresent()) {
                throw new ResourceNotFoundException(
                        "Member with " + memberName + " name does not exist");
            }
            Member member = memberOptional.get();
            MemberDTO memberDTO = memberMapper.memberToMemberDTO(member);
            //Check if member is eligible to participate in heist
            memberService.isValidHeistMember(
                    memberDTO,
                    requiredSkillsOfHeist,
                    heist.getStartTime(),
                    heist.getEndTime());

            //if validation does not throw errors make saves
            heist.setStatus(HeistStatus.READY);
            heist.addToMembers(member);
            member.addToHeists(heist);
            memberRepository.save(member);
            heistRepository.save(heist);
        }
    }
}
