package com.hackatonapi.HackatonRest.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EligibleMembersDTO {
    List<RequiredSkillDTO> skills = new ArrayList<>();
    Set<MemberDTO> members = new HashSet<>();
}
