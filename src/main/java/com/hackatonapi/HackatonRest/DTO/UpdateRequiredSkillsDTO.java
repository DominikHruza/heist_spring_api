package com.hackatonapi.HackatonRest.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class UpdateRequiredSkillsDTO {
    List<RequiredSkillDTO> skills;
}
