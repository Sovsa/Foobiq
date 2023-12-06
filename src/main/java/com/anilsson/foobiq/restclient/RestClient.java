package com.anilsson.foobiq.restclient;

import com.anilsson.foobiq.RestClientConfiguration;
import com.anilsson.foobiq.TrafikLabException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestClient {

    private ObjectMapper objectMapper = new ObjectMapper();
     private WebClient webClient;
     String apiKey;

     @Autowired
     RestClientConfiguration restClientConfiguration;

    public RestClient() {
    }

    public RestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    public void init() {
        apiKey = restClientConfiguration.getApiKey();
        webClient = WebClient.builder().baseUrl("https://api.sl.se/api2/LineData.json")
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(6 * 1048576))
                .build();
    }

    public List<LineDTO> getLines() {
        String responseString = webClient
                .get()
                .uri(Endpoint.LINE.getRequestUrl() + "&key=" + apiKey)
                .retrieve()
                .bodyToMono(String.class).block();

        try {
            Response response = getResponseFromResponseString(responseString);
            List<LineDTO> lines = getLineDTOFromJsonResponse(response);
            return lines;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StopPointDTO> getStopPoints() {
        String responseString = webClient
                .get()
                .uri(Endpoint.STOPPOINT.getRequestUrl() + "&key=" + apiKey)
                .retrieve()
                .bodyToMono(String.class).block();

        try {
            Response response = getResponseFromResponseString(responseString);
            List<StopPointDTO> stopPoints = getStopPointDTOFromJsonResponse(response);
            return stopPoints;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<JourneyDTO> getJournies() {
        String responseString = webClient
                .get()
                .uri(Endpoint.JOURNEY.getRequestUrl() + "&key=" + apiKey)
                .retrieve()
                .bodyToMono(String.class).block();

        try {
            Response response = getResponseFromResponseString(responseString);
            List<JourneyDTO> lines = getJourneyDTOFromJsonResponse(response);
            return lines;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JourneyDTO> getJourneyDTOFromJsonResponse(Response response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

    }

    private List<StopPointDTO> getStopPointDTOFromJsonResponse(Response response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
    }

    private List<LineDTO> getLineDTOFromJsonResponse(Response response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
    }

    private Response getResponseFromResponseString(String responseString) throws JsonProcessingException {
        Response response = objectMapper.readValue(responseString, new TypeReference<>() {
        });
        if(isRequestSuccessful(response)) {
            throw new TrafikLabException("Did not get a successful statuscode from Trafik Labs. Message: " + response.getMessage());
        }

        JsonNode node = objectMapper.readTree(responseString);
        JsonNode responseData = node.get("ResponseData");
        response.setBody(responseData.get("Result").toString());
        return response;
    }

    private boolean isRequestSuccessful(Response response) {
        return response.getStatusCode() != 0;
    }
}
