package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.entity.Skill;

public interface SkillService {
    Skill findOrInsertSkill(String name);
}
