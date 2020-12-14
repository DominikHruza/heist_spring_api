package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public abstract class SkillDTO extends NamedEntity {
    String level;

    public SkillDTO(String name, String level) {
        super(name);
        this.level = level;
    }
}
