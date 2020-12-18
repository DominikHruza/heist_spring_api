package com.hackatonapi.HackatonRest.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public enum MemberStatus {
    AVAILABLE, INCARCERATED, EXPIRED, RETIRED;

    public static MemberStatus getRandomStatus() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public static MemberStatus randBetweenTwo(MemberStatus first, MemberStatus last){
        List<MemberStatus> statusList = new ArrayList<>();
        statusList.add(first);
        statusList.add(last);

        int rnd = new Random().nextInt(statusList.size());
        return statusList.get(rnd);
    }
}
