package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.MemberDTO;
import com.hackatonapi.HackatonRest.DTO.MemberSkillDTO;
import com.hackatonapi.HackatonRest.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMapperImpl implements MemberMapper {

    SkillLevelMapperImpl skillLevelMapper;

    @Autowired
    public MemberMapperImpl(SkillLevelMapperImpl skillLevelMapper) {
        this.skillLevelMapper = skillLevelMapper;
    }

    @Override
    public MemberDTO memberToMemberDTO(Member member) {

        String name = member.getName();
        String email = member.getEmail();
        Character sex = member.getSex();
        String mainSkill = member.getMainSkill().getName();
        String status = member.getStatus().name();

        List<MemberSkillDTO> memberSkillDTOList =
                member
                .getMemberSkillLevels()
                .stream()
                .map(skillLevel -> {
                   return skillLevelMapper
                            .skillLevelToSkillLevelDTO(skillLevel);
                })
                .collect(Collectors.toList());

        return new MemberDTO(
                name,
                email,
                sex,
                mainSkill,
                status,
                memberSkillDTOList
        );
    }

}
