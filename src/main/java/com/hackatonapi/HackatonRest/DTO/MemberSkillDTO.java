package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Data
public class MemberSkillDTO extends SkillDTO {

    public MemberSkillDTO() { }

    public MemberSkillDTO(String name, String level) {
        super(name, level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberSkillDTO)) return false;
        MemberSkillDTO that = (MemberSkillDTO) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
