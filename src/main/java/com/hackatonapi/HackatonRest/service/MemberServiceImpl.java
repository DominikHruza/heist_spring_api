package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.MemberStatus;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.NotAMemberSkillException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    private boolean skillIsMemberSkill(Member member, Skill skill){
        return member.getMemberSkillLevels()
                .stream()
                .anyMatch(memberSkill ->
                        memberSkill
                                .getSkill()
                                .getName()
                                .equals(skill.getName()));
    }

}
