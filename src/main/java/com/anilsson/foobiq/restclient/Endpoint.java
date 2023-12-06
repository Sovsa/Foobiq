package com.anilsson.foobiq.restclient;

import com.anilsson.foobiq.RestClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public enum Endpoint {
    LINE("line", "BUS"),
    STOPPOINT("stop", null),
    JOURNEY("jour", null);

    private String model;
    private String transportModeCode;

    Endpoint(String model, String transportModeCode) {
        this.model = model;
        this.transportModeCode = transportModeCode;
    }

    public String getRequestUrl() {
        String requestUrl = "?model=" + model;
        if(transportModeCode != null && !transportModeCode.isEmpty()) {
            requestUrl = requestUrl + "&DefaultTransportModeCode=" + transportModeCode;
        }
        return requestUrl;
    }
}
