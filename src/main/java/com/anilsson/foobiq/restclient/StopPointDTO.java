package com.anilsson.foobiq.restclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record StopPointDTO( @JsonProperty("StopPointNumber") Integer stopPointNumber,
                            @JsonProperty("StopPointName") String stopPointName,
                            @JsonProperty("StopAreaTypeCode") String stopAreaTypeCodeName,
                            @JsonProperty("ZoneShortName") String zoneShortName) {

}
