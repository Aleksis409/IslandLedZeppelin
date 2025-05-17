package com.javarush.island.artemov.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeFormConfig {
    private String name;
    private Double weight;
    private Integer maxPerCell;
    private Integer maxSpeed;
    private Double foodToSaturate;
    private String image;

    public String getName() {
        return name;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getMaxPerCell() {
        return maxPerCell;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public Double getFoodToSaturate() {
        return foodToSaturate;
    }

    public String getImage() {
        return image;
    }
}
