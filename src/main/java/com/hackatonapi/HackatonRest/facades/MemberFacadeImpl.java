package com.hackatonapi.HackatonRest.facades;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.mappers.MemberMapper;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.service.MemberService;
import com.hackatonapi.HackatonRest.service.MemberSkillLevelService;
import com.hackatonapi.HackatonRest.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MemberFacadeImpl implements MemberFacade {

    MemberSkillLevelService memberSkillLevelService;
    MemberRepository memberRepository;
    MemberService memberService;
    SkillService skillService;


    @Autowired
    public MemberFacadeImpl(MemberSkillLevelService memberSkillLevelService,
                            MemberService memberService,
                            SkillService skillService,
                            MemberRepository memberRepository
                           ) {
        this.memberSkillLevelService = memberSkillLevelService;
        this.memberService = memberService;
        this.skillService = skillService;
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDTO addNewMember(MemberDTO memberDTO) {
        //Save if any new skills in dto
        memberDTO.getSkills()
                .stream()
                .forEach(memberSkillDTO ->
                        skillService.findOrInsertSkill(memberSkillDTO.getName()));

        //Save member
        MemberDTO savedMemberDto = memberService.addMember(memberDTO);

        //Set all skills
        List<MemberSkillDTO> memberSkillLevels =
                memberSkillLevelService
                        .bulkSaveMemberSkillLevel(memberDTO.getSkills(), memberDTO.getName());
        savedMemberDto.setSkills(memberSkillLevels);

        //Set main skill
        MainSkillDTO mainSkillDTO = memberService.setMainSkill(
                memberDTO.getName(), memberDTO.getMainSkill());

        savedMemberDto.setMainSkill(mainSkillDTO.getName());

        return savedMemberDto;

    }

    @Override
    public void updateSkillsAndMainSkill(UpdateMemberSkillsDTO skillsData, Long memberId) {
        //Find member
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if(!memberOpt.isPresent()){
            throw new ResourceNotFoundException("Member with id " + memberId + " does not exists.");
        }

        Member member = memberOpt.get();
        //Update skill list
        if (skillsData.getSkills().isPresent()) {
            List<MemberSkillDTO> memberSkillDTOS = skillsData.getSkills().get();
            memberSkillLevelService.bulkSaveMemberSkillLevel(memberSkillDTOS, member.getName());
        }

        //Update main skill
        if (skillsData.getMainSkill().isPresent()) {
            String mainSkillName = skillsData.getMainSkill().get();
            memberService.setMainSkill(member.getName(), mainSkillName);
        }
    }
}
