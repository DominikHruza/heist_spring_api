package com.hackatonapi.HackatonRest.controller;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.facades.HeistFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/heist")
public class HeistController {

    private final HeistFacade heistFacade;


    public HeistController(HeistFacade heistFacade) {
        this.heistFacade = heistFacade;
    }


    //SBSS-04: Add new heist
    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public HeistDTO addHeist(@RequestBody HeistDTO heistDTO){

       return heistFacade.makeNewHeist(heistDTO);
    }

    //SBSS-05: Update required skills
    @PatchMapping(value = "/{heistId}/skills")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRequiredSkills(
            @PathVariable("heistId") Long heistId,
            @RequestBody UpdateRequiredSkillsDTO requiredSkillDTOS) {

        heistFacade.updateRequiredSkills(heistId, requiredSkillDTOS);
    }

    //SBSS-06 Get eligible members
    @GetMapping(value = "/{heistId}/eligible_members")
    @ResponseStatus(HttpStatus.OK)
    public EligibleMembersDTO getEligibleMembers(@PathVariable Long heistId){
       return heistFacade.getEligibleMembers(heistId);
    }

    //SBSS-07 Confirm participants
    @PutMapping(value = "/{heist_id}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmParticipants(@PathVariable Long heist_id, @RequestBody ParticipantsDTO participants){
        heistFacade.confirmParticipants(heist_id, participants);
    }
}
