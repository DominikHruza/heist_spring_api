package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.DTO.HeistDTO;

public interface HeistService {
    HeistDTO saveNewHeist(HeistDTO heistDTO);
}
