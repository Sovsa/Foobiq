package com.anilsson.foobiq.service;

import com.anilsson.foobiq.restclient.StopPointDTO;

public record Stop(Integer stopPointNumber,
                   String stopPointName,
                   String stopAreaTypeCode,
                   String zoneShortName){

    public Stop(StopPointDTO dto) {
        this(dto.stopPointNumber(), dto.stopPointName(), dto.stopAreaTypeCodeName(), dto.zoneShortName());
    }
}
