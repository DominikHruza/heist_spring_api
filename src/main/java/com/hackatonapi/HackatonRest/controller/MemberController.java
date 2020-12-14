package com.hackatonapi.HackatonRest.controller;


import com.hackatonapi.HackatonRest.DTO.MemberDTO;
import com.hackatonapi.HackatonRest.DTO.UpdateMemberSkillsDTO;
import com.hackatonapi.HackatonRest.facades.MemberFacade;
import com.hackatonapi.HackatonRest.service.MemberService;
import com.hackatonapi.HackatonRest.service.MemberSkillLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberSkillLevelService memberSkillLevelService;
    private final MemberFacade memberFacade;

    @Autowired
    public MemberController(MemberService memberService,
                            MemberSkillLevelService memberSkillLevelService,
                            MemberFacade memberFacade) {
        this.memberService = memberService;
        this.memberSkillLevelService = memberSkillLevelService;
        this.memberFacade = memberFacade;
    }

    //SBSS-01: Add new member
    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDTO addMember(@RequestBody MemberDTO memberDTO) {

       MemberDTO savedMemberDto = memberFacade.addNewMember(memberDTO);
       return savedMemberDto;
    }

    //SBSS-02: Update member skills
    @PutMapping(value = "/{memberId}/skills")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMemberSkills(
            @PathVariable("memberId") Long memberId,
            @RequestBody UpdateMemberSkillsDTO memberSkillsDTO){
       memberFacade.updateSkillsAndMainSkill(memberSkillsDTO, memberId);
    }

    //SBSS-03: Delete member skill
    @DeleteMapping(value = "/{memberId}/skills/{skillName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemberSkill(
            @PathVariable("memberId") Long memberId,
            @PathVariable("skillName") String skillName){

        memberSkillLevelService.deleteMemberSkillLevel(memberId,skillName);
    }
}
