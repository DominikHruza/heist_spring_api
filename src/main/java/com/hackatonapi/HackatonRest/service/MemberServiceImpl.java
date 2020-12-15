package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.MemberSkillLevel;
import com.hackatonapi.HackatonRest.entity.MemberStatus;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.InvalidParticipantException;
import com.hackatonapi.HackatonRest.exception.NotAMemberSkillException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.helpers.Helpers;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             SkillRepository skillRepository,
                             MemberMapper memberMapper)
    {
        this.memberRepository = memberRepository;
        this.skillRepository = skillRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public MemberDTO addMember(MemberDTO memberDTO) {

        //Construct new member
        MemberStatus status = MemberStatus.valueOf(memberDTO.getStatus().toUpperCase());
        Member newMember = new Member(
                memberDTO.getName(),
                memberDTO.getEmail(),
                memberDTO.getSex(),
                status
        );

        try {
            newMember = memberRepository.save(newMember);

        } catch (Exception e){
             e.printStackTrace();
            throw new DuplicateResourceEntryException(
                    "Member with email " + memberDTO.getEmail() + " already exists");
        }

        return new MemberDTO(
                newMember.getName(),
                newMember.getEmail(),
                newMember.getSex(),
                newMember.getStatus().name()
        );
    }


    @Override
    public MainSkillDTO setMainSkill(String memberName, String skillName){

        Optional<Member> memberOptional = memberRepository.findByName(memberName);
        Optional<Skill> skillOptional = skillRepository.findByName(skillName);

        if(!memberOptional.isPresent()){
            throw new ResourceNotFoundException("Member with name " + memberName + " does not exists");
        }
        if(!skillOptional.isPresent()){
            throw new ResourceNotFoundException("Skill with name " + skillName + " does not exists");
        }
        //Check if skill belongs to this member
        Member member = memberOptional.get();
        Skill skill = skillOptional.get();

        boolean skillBelongsToMember= skillIsMemberSkill(member, skill);
        if (!skillBelongsToMember){
           throw new NotAMemberSkillException("Selected main skill does not belong to this member");
        }

        member.setMainSkill(skill);
        memberRepository.save(member);

        return new MainSkillDTO(member.getMainSkill().getName());
    }

    @Override
    public Set<MemberDTO> findMembersWithRequiredSkills(List<RequiredSkillDTO> requiredSkillDTOS) {

        Set<MemberDTO> matchingMembersDTOs = new HashSet<>();

        //Loop list of required skills
        requiredSkillDTOS.stream().forEach(requiredSkillDTO -> {
            //Get all members that match criteria
            List<Member> foundMembers =
                    memberRepository
                            .findBySkillNameAndLevel(
                                    requiredSkillDTO.getName(),
                                    requiredSkillDTO.getLevel());

            //Loop matched members, turn to dtos and send to dto set
            foundMembers
                    .stream()
                    .forEach(found ->
                            matchingMembersDTOs.add(
                                    memberMapper.memberToMemberDTO(found)));
        });

        return matchingMembersDTOs;
    }
    @Override
    public boolean isValidHeistMember(
            MemberDTO memberDTO,
            List<RequiredSkillDTO> requiredSkillDTOS,
            ZonedDateTime heistStart) {

        Optional<Member> memberOptional = memberRepository.findByName(memberDTO.getName());
        if(!memberOptional.isPresent()){
            throw new ResourceNotFoundException("Member with name " + memberDTO.getName() + " does not exists");
        }
        Member member = memberOptional.get();

        //Find at least one valid skill
        boolean hasValidSkills = hasRequiredSkills(
                memberDTO.getSkills(),
                requiredSkillDTOS);
        if(!hasValidSkills){
            throw new InvalidParticipantException(
                    "Member with name " + memberDTO.getName() + " does not have valid skills");
        }

        //Validate member status
        boolean hasValidStatus = hasValidStatus(memberDTO.getStatus());
        if(!hasValidStatus){
            throw new InvalidParticipantException(
                    "Member with name " + memberDTO.getName() + " does not have valid status");
        }

        //Check if member participates in another heist at that time period
        boolean hasValidTiming = hasValidTiming(heistStart, member);
        if(!hasValidTiming){
            throw new InvalidParticipantException(
                    "Member with name " + memberDTO.getName() + " is not available at that time period.");
        }

        return true;
    }


    private boolean hasRequiredSkills(
            List<MemberSkillDTO> memberSkillDTOS,
            List<RequiredSkillDTO> requiredSkillDTOS) {

        //Check if member has at least one valid skill (name and level)
        return requiredSkillDTOS
                .stream()
                .anyMatch(requiredSkillDTO ->
                        hasValidHeistSkill(memberSkillDTOS, requiredSkillDTO));
    }

    private boolean skillIsMemberSkill(Member member, Skill skill){
        return member.getMemberSkillLevels()
                .stream()
                .anyMatch(memberSkill ->
                        memberSkill
                                .getSkill()
                                .getName()
                                .equals(skill.getName()));
    }

    private boolean hasValidHeistSkill(List<MemberSkillDTO> memberSkillDTOS,
                                     RequiredSkillDTO requiredSkillDTO) {

        for (MemberSkillDTO memberSkill : memberSkillDTOS) {

            if (memberSkill.getLevel().equals(requiredSkillDTO.getLevel()) && //Check level
                memberSkill.getName().equals(requiredSkillDTO.getName())) { //Check name
                return true;
            }
        }

        return false;
    }

    private boolean hasValidStatus(String status){
        if(status.equalsIgnoreCase(MemberStatus.AVAILABLE.name()) ||
                status.equalsIgnoreCase(MemberStatus.RETIRED.name())) {
            return true;
        }
        return false;
    }

    private boolean hasValidTiming(ZonedDateTime startTime, Member member){

        return member.getHeists().stream().anyMatch(heist -> {
            return Helpers.isWithinRange(
                    startTime,
                    heist.getStartTime(),
                    heist.getEndTime());
        });
    }
}
