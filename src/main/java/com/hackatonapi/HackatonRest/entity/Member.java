package com.hackatonapi.HackatonRest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends NamedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private Character sex;

    @Enumerated(value=EnumType.STRING)
    private MemberStatus status;

    @ManyToOne
    private Skill mainSkill;

   @OneToMany(mappedBy = "member")
   List<MemberSkillLevel> memberSkillLevels = new ArrayList<>();

    public Member() {}

    public Member(
            String name,
            String email,
            Character sex,
            MemberStatus status)
    {
        super(name);
        this.email = email;
        this.sex = sex;
        this.status = status;
    }

    public void addMemberSkillLevel(MemberSkillLevel memberSkillLevel){
        memberSkillLevels.add(memberSkillLevel);
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }
}
