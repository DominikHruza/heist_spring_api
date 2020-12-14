package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.entity.RequiredSkill;

public interface RequiredSkillMapper {
    RequiredSkillDTO requiredSkillToRequiredSkillDTO(RequiredSkill requiredSkill);
}
