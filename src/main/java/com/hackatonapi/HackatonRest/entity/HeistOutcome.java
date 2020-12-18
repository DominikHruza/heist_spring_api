package com.hackatonapi.HackatonRest.entity;

import java.util.Random;

public enum HeistOutcome {
    FAILED, SUCCEEDED;

    public static HeistOutcome getRandomOutcome() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
