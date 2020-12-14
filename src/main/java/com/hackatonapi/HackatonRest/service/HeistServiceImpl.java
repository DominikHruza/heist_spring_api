package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;
import com.hackatonapi.HackatonRest.entity.Heist;
import com.hackatonapi.HackatonRest.entity.RequiredSkill;
import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.exception.DuplicateResourceEntryException;
import com.hackatonapi.HackatonRest.exception.HeistTimestampException;
import com.hackatonapi.HackatonRest.mappers.HeistMapper;
import com.hackatonapi.HackatonRest.repository.HeistRepository;
import com.hackatonapi.HackatonRest.repository.RequiredSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeistServiceImpl implements HeistService {

    HeistRepository heistRepository;
    HeistMapper heistMapper;

    @Autowired
    public HeistServiceImpl(HeistRepository heistRepository,
                            HeistMapper heistMapper) {
        this.heistRepository = heistRepository;
        this.heistMapper = heistMapper;
    }

    @Override
    public HeistDTO saveNewHeist(HeistDTO heistDTO) {
        checkTimestampValidity(heistDTO.getStartTime(), heistDTO.getEndTime());

        Heist newHeist = new Heist(
                heistDTO.getName(),
                heistDTO.getLocation(),
                heistDTO.getStartTime(),
                heistDTO.getEndTime()
        );

        try {
            heistRepository.save(newHeist);
        } catch (Exception e){
            throw new DuplicateResourceEntryException(
                    "Member with name " + heistDTO.getName() + " already exists");
        }

        HeistDTO newHeistDTO = heistMapper.heistToHeistDTO(newHeist);

        return newHeistDTO;
    }

    private boolean checkTimestampValidity(ZonedDateTime start, ZonedDateTime end){
        if(start.isAfter(end)){
           throw new HeistTimestampException("Heist start time is after end time.");
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));
        if(now.isAfter(end)){
            throw new HeistTimestampException("Heist end time is before current time.");
        }

        return true;
    }
}
