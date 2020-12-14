package com.hackatonapi.HackatonRest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Heist extends NamedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @OneToMany(mappedBy = "heist")
    List<RequiredSkill> requiredSkillList = new ArrayList<>();

    public Heist() {}

    public Heist(String name, String location, ZonedDateTime startTime, ZonedDateTime endTime) {
        super(name);
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
