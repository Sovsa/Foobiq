package com.anilsson.foobiq.restclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record JourneyDTO(@JsonProperty("LineNumber")Integer lineNumber,
                         @JsonProperty("DirectionCode")Integer direction,
                         @JsonProperty("JourneyPatternPointNumber")Integer stopPointNumber) {
}
