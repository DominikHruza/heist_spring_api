package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.entity.RequiredSkill;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.ResourceNotFoundException;
import com.hackatonapi.HackatonRest.helpers.Helpers;
import com.hackatonapi.HackatonRest.mappers.RequiredSkillMapper;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.repository.RequiredSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequiredSkillsServiceImpl implements RequiredSkillsService {

    HeistRepository heistRepository;
    SkillService skillService;
    RequiredSkillRepository requiredSkillRepository;
    RequiredSkillMapper requiredSkillMapper;


    public RequiredSkillsServiceImpl(
            HeistRepository heistRepository,
            SkillService skillService,
            RequiredSkillRepository requiredSkillRepository,
            RequiredSkillMapper requiredSkillMapper) {

        this.heistRepository = heistRepository;
        this.skillService = skillService;
        this.requiredSkillRepository = requiredSkillRepository;
        this.requiredSkillMapper = requiredSkillMapper;
    }

    @Override
    @Transactional
    public List<RequiredSkillDTO> bulkAddRequiredSkills(
            String heistName,
            List<RequiredSkillDTO> requiredSkillDTOs) {

        Optional<Heist> heistOpt = heistRepository.findByName(heistName);
        if (!heistOpt.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with name " + heistName + " does not exist");
        }

        return saveToRequiredSkillList(heistOpt.get(), requiredSkillDTOs);
    }

    @Override
    public List<RequiredSkillDTO> findRequiredSkillsOfHeist(Long heistId) {

        List<RequiredSkill> requiredSkills = requiredSkillRepository.findByHeistId(heistId);

        return requiredSkills.stream().map(
                requiredSkill ->
                       requiredSkillMapper
                               .requiredSkillToRequiredSkillDTO(requiredSkill))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequiredSkillDTO> getRequiredSkills(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);
        if(!heistOptional.isPresent()){
            throw new ResourceNotFoundException(
                    "Heist with id " + heistId + " does not exist."
            );
        }
        Heist heist = heistOptional.get();

        List<RequiredSkillDTO> requiredSkillDTOS = heist
                .getRequiredSkillList()
                .stream()
                .map(requiredSkill ->
                        requiredSkillMapper
                                .requiredSkillToRequiredSkillDTO(requiredSkill))
                .collect(Collectors.toList());

        return requiredSkillDTOS ;
    }

    private List<RequiredSkillDTO> saveToRequiredSkillList(Heist heist ,List<RequiredSkillDTO> requiredSkillDTOs){
        if(Helpers.checkForDuplicates(requiredSkillDTOs)){
            throw new DuplicateResourceEntryException(
                    "Required heist skills list contains duplicates.");
        }

        List<RequiredSkillDTO> updatedRequiredSkillDtos = requiredSkillDTOs
                .stream()
                .map(requiredSkillDTO -> {
                    RequiredSkill savedRequiredSkill;

                    Optional<RequiredSkill> requiredSkillOpt = requiredSkillRepository.findByHeistAndSkill(
                                    heist.getId(), requiredSkillDTO.getName());

                    //if skill already exists in db make update else create new from dto
                    if(requiredSkillOpt.isPresent()){
                       savedRequiredSkill = updateRequiredSkill(requiredSkillOpt.get(), requiredSkillDTO);
                    } else {
                        savedRequiredSkill = createNewRequiredSkill(heist, requiredSkillDTO);
                    }

                    return requiredSkillMapper.requiredSkillToRequiredSkillDTO(
                            savedRequiredSkill
                    ) ;
                })
                .collect(Collectors.toList());

        return updatedRequiredSkillDtos;
    }

    private RequiredSkill createNewRequiredSkill(Heist heist,  RequiredSkillDTO requiredSkillDTO){

        //Find or create new skill if first usage
        Skill skill = skillService.findOrInsertSkill(
                requiredSkillDTO.getName());

        //Construct new required skill entity
        RequiredSkill newRequiredSkill = new RequiredSkill(
                requiredSkillDTO.getLevel(),
                requiredSkillDTO.getMembers(),
                skill,
                heist
        );

        return requiredSkillRepository.save(newRequiredSkill);
    }

    private RequiredSkill updateRequiredSkill(RequiredSkill requiredSkill, RequiredSkillDTO requiredSkillDTO) {

        requiredSkill.setLevel(requiredSkillDTO.getLevel());
        requiredSkill.setMembers(requiredSkillDTO.getMembers());
        return requiredSkillRepository.save(requiredSkill);
    }
}
