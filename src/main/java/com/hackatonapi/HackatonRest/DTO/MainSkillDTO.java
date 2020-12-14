package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class MainSkillDTO extends NamedEntity {

    public MainSkillDTO(String name) {
        super(name);
    }
}
