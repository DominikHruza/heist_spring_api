package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.MemberSkillDTO;
import com.hackatonapi.HackatonRest.entity.MemberSkillLevel;
import org.springframework.stereotype.Component;

@Component
public class SkillLevelMapperImpl implements SkillLevelMapper {
    @Override
    public MemberSkillDTO skillLevelToSkillLevelDTO(MemberSkillLevel skillLevel) {
        MemberSkillDTO memberSkillDTO = new MemberSkillDTO();
        memberSkillDTO.setLevel(skillLevel.getLevel());
        memberSkillDTO.setName(skillLevel.getSkill().getName());

        return memberSkillDTO;
    }
}
