package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.*;
import com.hackatonapi.HackatonRest.entity.HeistOutcome;
import com.hackatonapi.HackatonRest.entity.HeistStatus;

import java.util.List;

public interface HeistService {
    HeistDTO getHeist(Long id);
    HeistDTO saveNewHeist(HeistDTO heistDTO);
    void startHeist(Long heistId);
    List<CurrentHeistMemberDTO> getHeistMembers(Long heist_id);
    HeistStatusDTO getHeistStatus(Long heistId);
    void changeStatus(Long id, HeistStatus status);
    void addMemberToHeist(Long id, String memberName);
    Double calculateMembersPercentage(Long heistId);
    void setHeistOutcome(Long id, HeistOutcome outcome);
    void setHeistOutcome(Long id);
}
