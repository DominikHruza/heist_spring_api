package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.MemberSkillDTO;
import com.hackatonapi.HackatonRest.DTO.MemberSkillsInfoDTO;
import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateMemberSkillsDTO;
import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.entity.Member;
import com.hackatonapi.HackatonRest.entity.MemberSkillLevel;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.helpers.Helpers;
import com.hackatonapi.HackatonRest.mappers.SkillLevelMapperImpl;
import com.hackatonapi.HackatonRest.repository.MemberRepository;
import com.hackatonapi.HackatonRest.repository.MemberSkillLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberSkillLevelServiceImpl implements MemberSkillLevelService {
    private final MemberRepository memberRepository;
    private final MemberSkillLevelRepository memberSkillLevelRepository;
    private final SkillLevelMapperImpl skillLevelMapper;
    private final SkillService skillService;


    public MemberSkillLevelServiceImpl(
            MemberRepository memberRepository,
            MemberSkillLevelRepository memberSkillLevelRepository,
            SkillLevelMapperImpl skillLevelMapper,
            SkillService skillService) {
        this.memberRepository = memberRepository;
        this.memberSkillLevelRepository = memberSkillLevelRepository;
        this.skillLevelMapper = skillLevelMapper;
        this.skillService = skillService;
    }

    @Override
    public MemberSkillDTO saveMemberSkillLevel(MemberSkillDTO memberSkillDTO, String memberName) {
        //Find member in db
        Optional<Member> memberOpt = memberRepository.findByName(memberName);
        if (!memberOpt.isPresent()) {
            throw new ResourceNotFoundException("Member with id " + memberName + " does not exist.");
        }
        Member member = memberOpt.get();

        //Check if new or existing member skill
        Optional<MemberSkillLevel> oldMemberSkillOptional =
                memberSkillLevelRepository.findBySkillNameAndMemberId(memberSkillDTO.getName(), member.getId());

        //If existing skill, update skill level and exit function
        if (oldMemberSkillOptional.isPresent()) {
            MemberSkillLevel oldMemberSkill = oldMemberSkillOptional.get();
            return updateSkillLevel(oldMemberSkill, memberSkillDTO);
        }

        //If this is new member skill
        MemberSkillLevel memberSkillLevel = createMemberSkill(member, memberSkillDTO);
        //Set saved entity to member
        member.addMemberSkillLevel(memberSkillLevel);
        //Map to dto
        return skillLevelMapper.skillLevelToSkillLevelDTO(memberSkillLevel);
    }

    @Override
    public MemberSkillsInfoDTO getMemberSkills(Long memberId) {
        //Find member in db
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new ResourceNotFoundException("Member with id " + memberId + " does not exist.");
        }
        Member member = memberOpt.get();


        List<MemberSkillDTO> memberSkillDTOS = member
                .getMemberSkillLevels()
                .stream()
                .map(memberSkillLevel ->
                        skillLevelMapper.skillLevelToSkillLevelDTO(memberSkillLevel))
                .collect(Collectors.toList());


        return new MemberSkillsInfoDTO(
                memberSkillDTOS,
                member.getMainSkill().getName()
        );
    }

    @Override
    public List<MemberSkillDTO> bulkSaveMemberSkillLevel(
            List<MemberSkillDTO> memberSkillDTOS, String memberName) {

        //Check duplicates in DTO
        if (Helpers.checkForDuplicates(memberSkillDTOS)) {
            throw new DuplicateResourceEntryException(
                    "Member skills list contains duplicates.");
        }

        List<MemberSkillDTO> memberSkillLevelsDTOS = memberSkillDTOS
                .stream()
                .map(skillLevelDTO -> {
                    return saveMemberSkillLevel(skillLevelDTO, memberName);
                }).collect(Collectors.toList());
        return memberSkillLevelsDTOS;
    }

    @Override
    public void deleteMemberSkillLevel(Long memberId, String skillName) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            throw new ResourceNotFoundException("Member with email " + memberId + " does not exists");
        }

        Member member = memberOptional.get();
        Optional<MemberSkillLevel> memberSkillLevelOptional =
                member
                        .getMemberSkillLevels()
                        .stream()
                        .filter(skillLevel ->
                                skillLevel
                                        .getSkill()
                                        .getName()
                                        .equals(skillName))
                        .findFirst();
        if (!memberSkillLevelOptional.isPresent()) {
            throw new ResourceNotFoundException("Skill " + skillName + " does not belong to this member");
        }

        MemberSkillLevel memberSkillLevel = memberSkillLevelOptional.get();
        memberSkillLevelRepository.delete(memberSkillLevel);
        member.getMemberSkillLevels().remove(memberSkillLevel);
    }



    private MemberSkillLevel createMemberSkill(Member member, MemberSkillDTO memberSkillDTO) {
        //Find skill in db or insert to skill table if this is first usage
        Skill skill = skillService.findOrInsertSkill(memberSkillDTO.getName());
        //Create entity to be saved to member skills table
        MemberSkillLevel memberSkill = new MemberSkillLevel(
                skill,
                member,
                memberSkillDTO.getLevel());
        //Save entity to table
        MemberSkillLevel memberSkillLevel =
                memberSkillLevelRepository.save(memberSkill);

        return memberSkillLevel;
    }

    private MemberSkillDTO updateSkillLevel(MemberSkillLevel memberSkillLevel, MemberSkillDTO memberSkillDTO) {
        memberSkillLevel.setLevel(memberSkillDTO.getLevel());
        memberSkillLevelRepository.save(memberSkillLevel);
        return skillLevelMapper.skillLevelToSkillLevelDTO(memberSkillLevel);
    }
}
