package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;

import java.util.List;

public interface RequiredSkillsService {
    List<RequiredSkillDTO> bulkAddRequiredSkills(
            String heistName,
            List<RequiredSkillDTO> requiredSkillDTOs);

    List<RequiredSkillDTO> findRequiredSkillsOfHeist(Long heistId);
}
