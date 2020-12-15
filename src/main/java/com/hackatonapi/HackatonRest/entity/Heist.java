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
    @Enumerated(value = EnumType.STRING)
    private HeistStatus status;

    @OneToMany(mappedBy = "heist")
    List<RequiredSkill> requiredSkillList = new ArrayList<>();

    @ManyToMany(mappedBy = "heists")
    List<Member> members = new ArrayList<>();

    public Heist() {}

    public Heist(String name, String location, ZonedDateTime startTime, ZonedDateTime endTime, HeistStatus status) {
        super(name);
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public void addToMembers(Member member){
        members.add(member);
    }
}
