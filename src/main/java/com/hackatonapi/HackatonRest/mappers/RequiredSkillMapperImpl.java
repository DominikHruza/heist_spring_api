package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.entity.RequiredSkill;
import org.springframework.stereotype.Component;

@Component
public class RequiredSkillMapperImpl implements RequiredSkillMapper {
    @Override
    public RequiredSkillDTO requiredSkillToRequiredSkillDTO(
            RequiredSkill requiredSkill) {

        return new RequiredSkillDTO(
                requiredSkill.getSkill().getName(),
                requiredSkill.getLevel(),
                requiredSkill.getMembers()
        );
    }
}
