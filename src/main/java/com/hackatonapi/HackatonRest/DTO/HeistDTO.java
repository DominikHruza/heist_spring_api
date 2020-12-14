package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class HeistDTO extends NamedEntity {

    private String location;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    List<RequiredSkillDTO> skills = new ArrayList<>();


    public HeistDTO(
            String name,
            String location,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            List<RequiredSkillDTO> skills) {
        super(name);
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.skills = skills;
    }
}
