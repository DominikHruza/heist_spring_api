package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Data
public class MemberDTO extends NamedEntity {
    private String email;
    private Character sex;
    private String mainSkill;
    private String status;
    private List<MemberSkillDTO> skills;

    public MemberDTO() {
    }

    public MemberDTO(
            String name,
            String email,
            Character sex,
            String mainSkill,
            String status,
            List<MemberSkillDTO> skills
    ) {
        super(name);
        this.email = email;
        this.sex = sex;
        this.mainSkill = mainSkill;
        this.status = status;
        this.skills = skills;
    }

    public MemberDTO(
            String name,
            String email,
            Character sex,
            String status
    ) {
        super(name);
        this.email = email;
        this.sex = sex;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberDTO)) return false;
        if (!super.equals(o)) return false;
        MemberDTO memberDTO = (MemberDTO) o;
        return getEmail().equals(memberDTO.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail());
    }
}
