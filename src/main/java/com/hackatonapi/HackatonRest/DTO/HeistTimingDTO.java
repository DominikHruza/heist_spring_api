package com.hackatonapi.HackatonRest.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class HeistTimingDTO {
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
