package com.example.corecard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "activity_status")
public class ActivityStatusEntity {
    @Id
    private String code;
    private String name;

}
