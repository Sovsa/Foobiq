package com.anilsson.foobiq;

import com.anilsson.foobiq.restclient.JourneyDTO;
import com.anilsson.foobiq.restclient.LineDTO;
import com.anilsson.foobiq.restclient.RestClient;
import com.anilsson.foobiq.restclient.StopPointDTO;
import com.anilsson.foobiq.service.Line;
import com.anilsson.foobiq.service.TrafikLabService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrafikLabServiceTest {

    RestClient restClient;

    @Before
    public void before() {
        restClient = mock(RestClient.class);
    }

    @Test
    public void testServiceClass() {
        List<LineDTO> lineDTOS = setupLineDTOs();
        List<StopPointDTO> stopPointDTOS = setupStopPointDTOs();
        List<JourneyDTO> journeyDTOS = setupJourneyDTOs(lineDTOS, stopPointDTOS);
        when(restClient.getLines()).thenReturn(lineDTOS);
        when(restClient.getJournies()).thenReturn(journeyDTOS);
        when(restClient.getStopPoints()).thenReturn(stopPointDTOS);
        TrafikLabService service = new TrafikLabService(restClient);

        List<Line> busLinesWithMostStops = service.getBusLinesWithMostStops(Integer.MAX_VALUE, 1);
        int size = busLinesWithMostStops.size();
        assertEquals(5, size);
        Line lineWithMostStops = busLinesWithMostStops.get(0);
        assertTrue(busLinesWithMostStops.get(0).getStops().size() > busLinesWithMostStops.get(1).getStops().size());

        journeyDTOS.add(new JourneyDTO(4, 1, 15));
        journeyDTOS.add(new JourneyDTO(4, 1, 14));
        journeyDTOS.add(new JourneyDTO(4, 1, 13));
        when(restClient.getJournies()).thenReturn(journeyDTOS);
        service = new TrafikLabService(restClient);

        busLinesWithMostStops = service.getBusLinesWithMostStops(Integer.MAX_VALUE, 1);
        assertTrue(busLinesWithMostStops.get(0).getStops().size() > busLinesWithMostStops.get(1).getStops().size());
        assertNotEquals(lineWithMostStops, busLinesWithMostStops.get(0));
    }

    private List<StopPointDTO> setupStopPointDTOs() {
        List<StopPointDTO> stopPointDTOS = new ArrayList<>();
        List<String> stopNames = getPossibleStopNames();
        for (int i = 1; i <= 15; i++) {
            stopPointDTOS.add(new StopPointDTO(i, stopNames.get(i), "BUSTERM", "A"));
        }
        return stopPointDTOS;
    }

    private List<JourneyDTO> setupJourneyDTOs(List<LineDTO> lineDTOS, List<StopPointDTO> stopPointDTOS) {
        List<JourneyDTO> journeyDTOS = new ArrayList<>();
        for (LineDTO lineDTO : lineDTOS) {
                for (int x = 0; x <= lineDTO.lineNumber(); x++) {
                    journeyDTOS.add(new JourneyDTO(lineDTO.lineNumber(), 1, stopPointDTOS.get(x).stopPointNumber()));
                }
        }
        return journeyDTOS;
    }

    private List<LineDTO> setupLineDTOs() {
        List<LineDTO> lineDTOs = new ArrayList<>();
        for (int i = 1; i <= 5 ; i++) {
            lineDTOs.add(new LineDTO(i, Integer.toString(i), "BUS"));
        }
        return lineDTOs;
    }

    private List<String> getPossibleStopNames() {
        return Arrays.asList("Vadholma vägskäl",
                "Lilla Sundsholmen",
                "Skånsta norra",
                "Skånsta",
                "Korsgärdesvägen",
                "Inneby",
                "Kårbodavägen",
                "Gärdsviks gård",
                "Åkersberga station",
                "Väsby vägskäl",
                "Stensvik",
                "Bystaviken",
                "Danderyds sjukhus",
                "Bolby",
                "Gärdsviks vägskäl",
                "Mjölnarström");
    }
}
