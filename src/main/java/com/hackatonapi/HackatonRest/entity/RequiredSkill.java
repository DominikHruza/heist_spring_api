package com.hackatonapi.HackatonRest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredSkill extends GradedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Heist heist;

    @ManyToOne
    private Skill skill;

    private Integer members;

    public RequiredSkill(String level, Integer members, Skill skill, Heist heist) {
        super(level);
        this.members = members;
        this.skill = skill;
        this.heist = heist;
    }

}
