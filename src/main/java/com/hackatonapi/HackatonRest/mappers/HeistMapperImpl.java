package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.entity.Heist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HeistMapperImpl implements HeistMapper {
    RequiredSkillMapper requiredSkillMapper;

    public HeistMapperImpl(RequiredSkillMapper requiredSkillMapper) {
        this.requiredSkillMapper = requiredSkillMapper;
    }

    @Override
    public HeistDTO heistToHeistDTO(Heist heist) {
        List<RequiredSkillDTO> requiredSkillDTOS = heist
                .getRequiredSkillList()
                .stream()
                .map(requiredSkill ->
                     requiredSkillMapper
                                .requiredSkillToRequiredSkillDTO(requiredSkill))
                .collect(Collectors.toList());

        return new HeistDTO(
                heist.getName(),
                heist.getLocation(),
                heist.getStartTime(),
                heist.getEndTime(),
                requiredSkillDTOS
        );
    }
}
