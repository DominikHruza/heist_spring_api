package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.MemberSkillDTO;
import com.hackatonapi.HackatonRest.entity.MemberSkillLevel;

public interface SkillLevelMapper {
    MemberSkillDTO skillLevelToSkillLevelDTO(MemberSkillLevel skillLevel);
}
