package com.hackatonapi.HackatonRest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberSkillLevel extends GradedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Skill skill;

    @ManyToOne
    Member member;

    public MemberSkillLevel() {}

    public MemberSkillLevel(Skill skill, Member member,String level) {
        super(level);
        this.skill = skill;
        this.member = member;
    }
}
