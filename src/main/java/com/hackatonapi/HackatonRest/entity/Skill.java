package com.hackatonapi.HackatonRest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Skill extends NamedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "mainSkill")
    List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "skill")
    List<MemberSkillLevel> memberSkillLevels = new ArrayList<>();

    @OneToMany(mappedBy = "skill")
    List<RequiredSkill> requiredSkillList = new ArrayList<>();

    public Skill() {}

    public Skill(String name) {
        super(name);
    }
}
