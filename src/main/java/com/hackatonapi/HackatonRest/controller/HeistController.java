package com.hackatonapi.HackatonRest.controller;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.facades.HeistFacade;
import com.hackatonapi.HackatonRest.service.HeistService;
import com.hackatonapi.HackatonRest.service.RequiredSkillsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heist")
public class HeistController {

    private final HeistFacade heistFacade;
    private final HeistService heistService;
    private final RequiredSkillsService requiredSkillsService;


    public HeistController(HeistFacade heistFacade,
                           HeistService heistService,
                           RequiredSkillsService requiredSkillsService) {
        this.heistFacade = heistFacade;
        this.heistService = heistService;
        this.requiredSkillsService = requiredSkillsService;
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

    //SBSS-08 Start heist manually
    @PutMapping(value = "/{heist_id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void startHeistManually(@PathVariable Long heist_id){
        heistService.startHeist(heist_id);
    }

    //SBSS-09-3: Get heist
    @GetMapping(value = "/{heist_id}")
    @ResponseStatus(HttpStatus.OK)
    public HeistDTO getMember(@PathVariable Long heist_id){
        return heistService.getHeist(heist_id);
    }

    //SBSS-09-4: Get heist members
    @GetMapping(value = "/{heist_id}/members")
    @ResponseStatus(HttpStatus.OK)
    public List<CurrentHeistMemberDTO> getHeistMembers(@PathVariable Long heist_id){
        return heistService.getHeistMembers(heist_id);
    }

    //SBSS-09-5: Get heist required skills
    @GetMapping(value = "/{heist_id}/skills")
    @ResponseStatus(HttpStatus.OK)
    public List<RequiredSkillDTO> getRequiredSkills(@PathVariable Long heist_id){
        return requiredSkillsService.getRequiredSkills(heist_id);
    }

    //SBSS-9-6: Get heist required skills
    @GetMapping(value = "/{heist_id}/status")
    @ResponseStatus(HttpStatus.OK)
    public HeistStatusDTO getHeistStatus(@PathVariable Long heist_id){
        return heistService.getHeistStatus(heist_id);
    }
}
