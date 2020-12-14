package com.hackatonapi.HackatonRest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class NamedEntity {

    @Column(unique = true)
    private String name;

    public NamedEntity(String name) {
        this.name = name;
    }
}
