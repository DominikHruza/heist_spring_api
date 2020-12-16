package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.CurrentHeistMemberDTO;
import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.DTO.HeistStatusDTO;
import com.hackatonapi.HackatonRest.DTO.RequiredSkillDTO;

import java.util.List;

public interface HeistService {
    HeistDTO getHeist(Long id);
    HeistDTO saveNewHeist(HeistDTO heistDTO);
    void startHeist(Long heistId);
    List<CurrentHeistMemberDTO> getHeistMembers(Long heist_id);
    HeistStatusDTO getHeistStatus(Long heistId);
}
