package com.example.controller.tda.list.graph;

public class Adycencia {
    private Integer destination;
    private Float weight;


    public Adycencia(Integer destination, Float weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public Adycencia(){}

    public Integer getDestination() {
        return destination;
    }
    public void setDestination(Integer destination) {
        this.destination = destination;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
