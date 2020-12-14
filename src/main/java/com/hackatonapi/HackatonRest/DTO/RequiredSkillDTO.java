package com.hackatonapi.HackatonRest.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Data
@NoArgsConstructor
public class RequiredSkillDTO extends SkillDTO {

    Integer members;

    public RequiredSkillDTO(String name, String level, Integer members) {
        super(name, level);
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequiredSkillDTO)) return false;
        if (!super.equals(o)) return false;
        RequiredSkillDTO that = (RequiredSkillDTO) o;
        return ((RequiredSkillDTO) o).getLevel().equals(this.getLevel()) &&
                ((RequiredSkillDTO) o).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
