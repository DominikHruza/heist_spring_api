package com.hackatonapi.HackatonRest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class GradedEntity {

    @Column(length = 10)
    private String level;

    public GradedEntity(String level) {
        this.level = level;
    }
}
