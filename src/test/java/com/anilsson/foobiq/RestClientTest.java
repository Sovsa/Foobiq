package com.anilsson.foobiq;

import com.anilsson.foobiq.restclient.JourneyDTO;
import com.anilsson.foobiq.restclient.LineDTO;
import com.anilsson.foobiq.restclient.RestClient;
import com.anilsson.foobiq.restclient.StopPointDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestClientTest {

    private WebClient.RequestHeadersUriSpec requestBodyUriMock;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseMock;
    private WebClient webClientMock;

    @Before
    public void beforeClass() {
        requestBodyUriMock = mock(WebClient.RequestHeadersUriSpec.class);
        responseMock = mock(WebClient.ResponseSpec.class);
        webClientMock = mock(WebClient.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(webClientMock.get()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseMock);
    }

    @Test
    public void testRestlient() {
        RestClient restClient = new RestClient(webClientMock);

        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(getSuccessfulLineResponseString()));
        LineDTO lineDTO = restClient.getLines().get(0);
        assertEquals(lineDTO.lineNumber(), (Integer) 995);
        assertEquals(lineDTO.lineDesignation(), "UL805");
        assertEquals(lineDTO.defaultTransportModeCode(), "BUS");

        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(getSuccessfulStopPointResponseString()));
        StopPointDTO stopPointDTO = restClient.getStopPoints().get(0);
        assertEquals(stopPointDTO.stopPointNumber(), (Integer) 666);
        assertEquals(stopPointDTO.stopPointName(), "TestStop");
        assertEquals(stopPointDTO.stopAreaTypeCodeName(), "BUSTERM");
        assertEquals(stopPointDTO.zoneShortName(), "A");

        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(getSuccessfulJourneyPatternPointOnLineResponseString()));
        JourneyDTO journeyDTO = restClient.getJournies().get(0);
        assertEquals(journeyDTO.lineNumber(), (Integer) 1);
        assertEquals(journeyDTO.stopPointNumber(), (Integer) 10008);
        assertEquals(journeyDTO.direction(), (Integer) 1);
        assertEquals(journeyDTO.direction(), (Integer) 1);

        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(getUnsuccessfulResponseString()));
        assertThrows(TrafikLabException.class, () -> restClient.getLines());
    }

    private String getUnsuccessfulResponseString() {
        return """
                {
                    "StatusCode": 1,
                    "Message": "test",
                    "ExecutionTime": 372,
                    "ResponseData": {
                        "Version": "2023-12-03 00:13",
                        "Type": "Line",
                        "Result": [
                            {
                                "LineNumber": "995",
                                "LineDesignation": "UL805",
                                "DefaultTransportMode": "",
                                "DefaultTransportModeCode": "BUS",
                                "LastModifiedUtcDateTime": "2007-08-24 00:00:00.000",
                                "ExistsFromDate": "2007-08-24 00:00:00.000"
                            }
                        ]
                    }
                }""";
    }

    private String getSuccessfulLineResponseString() {
        return """
                {
                    "StatusCode": 0,
                    "Message": null,
                    "ExecutionTime": 372,
                    "ResponseData": {
                        "Version": "2030-12-24 15:00",
                        "Type": "Line",
                        "Result": [
                            {
                                "LineNumber": "995",
                                "LineDesignation": "UL805",
                                "DefaultTransportMode": "",
                                "DefaultTransportModeCode": "BUS",
                                "LastModifiedUtcDateTime": "2007-08-24 00:00:00.000",
                                "ExistsFromDate": "2007-08-24 00:00:00.000"
                            }
                        ]
                    }
                }""";
    }

    private String getSuccessfulStopPointResponseString() {
        return """
                {
                    "StatusCode": 0,
                    "Message": null,
                    "ExecutionTime": 380,
                    "ResponseData": {
                        "Version": "2030-12-24 15:00",
                        "Type": "StopPoint",
                        "Result": [
                            {
                                "StopPointNumber": "666",
                                "StopPointName": "TestStop",
                                "StopAreaNumber": "777",
                                "LocationNorthingCoordinate": "59.3373571967995",
                                "LocationEastingCoordinate": "18.0214674159693",
                                "ZoneShortName": "A",
                                "StopAreaTypeCode": "BUSTERM",
                                "LastModifiedUtcDateTime": "2022-10-28 00:00:00.000",
                                "ExistsFromDate": "2022-10-28 00:00:00.000"
                            }
                        ]
                    }
                }""";
    }

    private String getSuccessfulJourneyPatternPointOnLineResponseString() {
        return """
                {
                    "StatusCode": 0,
                    "Message": null,
                    "ExecutionTime": 483,
                    "ResponseData": {
                        "Version": "2023-12-05 00:12",
                        "Type": "JourneyPatternPointOnLine",
                        "Result": [
                            {
                                "LineNumber": "1",
                                "DirectionCode": "1",
                                "JourneyPatternPointNumber": "10008",
                                "LastModifiedUtcDateTime": "2022-02-15 00:00:00.000",
                                "ExistsFromDate": "2022-02-15 00:00:00.000"
                            }
                        ]
                    }
                }""";
    }
}
