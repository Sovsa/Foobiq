package com.anilsson.foobiq.service;

import com.anilsson.foobiq.restclient.JourneyDTO;
import com.anilsson.foobiq.restclient.LineDTO;
import com.anilsson.foobiq.restclient.RestClient;
import com.anilsson.foobiq.restclient.StopPointDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Component
public class TrafikLabService {

    @Autowired
    RestClient restClient;
    List<LineDTO> lineResponse;
    List<JourneyDTO> journeyResponse;
    List<StopPointDTO> stopPointResponse;

    public TrafikLabService() {
    }

    public TrafikLabService(RestClient restClient) {
        this.restClient = restClient;
        this.lineResponse = restClient.getLines();
        this.journeyResponse = restClient.getJournies();
        this.stopPointResponse = restClient.getStopPoints();
    }

    @PostConstruct
    public void init() {
        this.lineResponse = restClient.getLines();
        this.journeyResponse = restClient.getJournies();
        this.stopPointResponse = restClient.getStopPoints();
    }

     public List<Line> getBusLinesWithMostStops(Integer limit, Integer direction) {
         List<Line> lines = getLines();
         Map<Integer, StopPointDTO> stopPointsMap = getIntegerStopPointDTOMap();
         Map<Integer, Set<Stop>> journeyDTOStopMap = getLineNumberToStops(stopPointsMap, direction);

         for (Line line : lines) {
             addStopsToLine(line, journeyDTOStopMap);
         }

         lines = lines.stream().filter(line -> line.getNumberOfStops() != null).
                 sorted(Comparator.comparing(Line::getNumberOfStops).reversed()).
                 limit(limit).
                 collect(Collectors.toList());

         return lines;
     }

    private static void addStopsToLine(Line line, Map<Integer, Set<Stop>> journeyDTOStopMap) {
        if(journeyDTOStopMap.get(line.getLineNumber()) == null) {
            return;
        }
        line.addStops(journeyDTOStopMap.get(line.getLineNumber()).stream().toList());
    }

    private Map<Integer, Set<Stop>> getLineNumberToStops(Map<Integer, StopPointDTO> stopPointsMap, Integer direction) {
        if(direction > 2) {
            throw new IllegalArgumentException("Direction cannot be larger than 2");
        }
        Map<Integer, Set<Stop>> journeyDTOStopMap = new HashMap<>();
        journeyResponse = journeyResponse.stream()
                .filter(journeyDTO -> journeyDTO.direction() == direction)
                .collect(Collectors.toList());
        for (JourneyDTO journey : journeyResponse) {
            StopPointDTO stopPointDTO = stopPointsMap.get(journey.stopPointNumber());
            if (stopPointDTO == null) {
                continue;
            }
            journeyDTOStopMap.computeIfAbsent(journey.lineNumber(), k -> new HashSet<>()).add(new Stop(stopPointDTO));
        }
        return journeyDTOStopMap;
    }

    private Map<Integer, StopPointDTO> getIntegerStopPointDTOMap() {
        Map<Integer, StopPointDTO> stopPointsMap = stopPointResponse.stream().
                collect(Collectors.toMap(StopPointDTO::stopPointNumber, Function.identity()));
        return stopPointsMap;
    }

    private List<Line> getLines() {
        List<Line> lines = lineResponse.stream()
                .map(lineDto -> new Line(lineDto))
                .collect(Collectors.toList());
        return lines;
    }
}
