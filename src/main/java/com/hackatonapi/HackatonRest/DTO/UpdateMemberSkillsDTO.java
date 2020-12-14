package com.hackatonapi.HackatonRest.DTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Data
public class UpdateMemberSkillsDTO {
    private Optional<List<MemberSkillDTO>> skills;
    private Optional<String> mainSkill;
}
