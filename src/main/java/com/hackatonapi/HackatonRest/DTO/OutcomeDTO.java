package com.hackatonapi.HackatonRest.DTO;

import com.hackatonapi.HackatonRest.entity.HeistOutcome;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class OutcomeDTO {
    String outcome;
}
