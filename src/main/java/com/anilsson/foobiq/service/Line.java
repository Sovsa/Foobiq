package com.anilsson.foobiq.service;

import com.anilsson.foobiq.restclient.LineDTO;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private List<Stop> stops;
    private Integer lineNumber;
    private String lineDesignation;
    private Integer numberOfStops;

    public Line(LineDTO dto) {
        this.lineNumber = dto.lineNumber();
        this.lineDesignation = dto.lineDesignation();
        stops = new ArrayList<>();
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineDesignation() {
        return lineDesignation;
    }

    public void setLineDesignation(String lineDesignation) {
        this.lineDesignation = lineDesignation;
    }

    public Integer getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(Integer numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    public void addStop(Stop stop) {
        this.stops.add(stop);
        this.numberOfStops = stops.size() + 1;
    }

    public void addStops(List<Stop> stops) {
        Integer stopsBeforeChange = this.stops.size();
        this.stops.addAll(stops);
        this.numberOfStops = stopsBeforeChange + stops.size();
    }
}
