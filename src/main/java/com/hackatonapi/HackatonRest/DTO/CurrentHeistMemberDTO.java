package com.hackatonapi.HackatonRest.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentHeistMemberDTO {
    String name;
    List<MemberSkillDTO> skills = new ArrayList<>();

}
