package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.entity.Skill;
import com.hackatonapi.HackatonRest.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill findOrInsertSkill(String name) {
        Skill skill = new Skill();
        Optional<Skill> skillOptional = skillRepository.findByName(name);
        if (!skillOptional.isPresent()) {
            skill = skillRepository.saveAndFlush(new Skill(name));
        } else {
            skill = skillOptional.get();
        }

        return skill;
    }
}
