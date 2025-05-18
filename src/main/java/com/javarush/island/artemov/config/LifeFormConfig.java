package com.javarush.island.artemov.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeFormConfig {
    private String name;
    private Double weight;
    private Integer maxPerCell;
    private Integer maxSpeed;
    private Double foodToSaturate;
    private String image;
}
