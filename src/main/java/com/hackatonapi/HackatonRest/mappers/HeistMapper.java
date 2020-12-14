package com.hackatonapi.HackatonRest.mappers;

import com.hackatonapi.HackatonRest.DTO.HeistDTO;
import com.hackatonapi.HackatonRest.entity.Heist;

public interface HeistMapper {
    HeistDTO heistToHeistDTO(Heist heist);
}
