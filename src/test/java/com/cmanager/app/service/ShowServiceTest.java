package com.cmanager.app.service;

import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.repository.ShowRepository;
import com.cmanager.app.application.service.ShowService;
import com.cmanager.app.integration.client.RestClientHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShowServiceTest {

    @Mock
    private ShowRepository repository;

    @Mock
    private RestClientHelper restClientHelper;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveShow_success() {

        ShowDto.Embedded.Episode episode = new ShowDto.Embedded.Episode(
                1L, "url", "Pilot", 1, 1, "type", "2020-01-01",
                "22:00", "2020-01-01T22:00:00", 60,
                new ShowDto.Rating(8.5), new ShowDto.Image("m", "o"),
                "summary", new ShowDto.Embedded.Episode.EpisodeLinks(
                new ShowDto.Embedded.Episode.EpisodeLinks.Self("href"),
                new ShowDto.Embedded.Episode.EpisodeLinks.Show("href", "name")
        )
        );

        ShowDto dto = new ShowDto(
                1L, "url", "Test Show", "Drama", "EN",
                List.of("Drama"), "Running", 60, 60,
                "2020-01-01", null, "site",
                new ShowDto.Schedule("22:00", List.of("Monday")),
                new ShowDto.Rating(8.5), 1,
                new ShowDto.Network(1L, "Net", new ShowDto.Network.Country("BR", "BR", "GMT-3"), "site"),
                null, null,
                new ShowDto.Externals(1, 2, "imdb"),
                new ShowDto.Image("m", "o"),
                "summary", 123L,
                new ShowDto.Links(new ShowDto.Links.Self("href"), null),
                new ShowDto.Embedded(List.of(episode))
        );
        when(restClientHelper.getForObject(anyString(), anyMap(), eq(ShowDto.class)))
                .thenReturn(dto);
        String result = showService.saveShow("Test Show");
        assertEquals("Show cadastrado com sucesso.", result);
        verify(repository, times(1)).save(any());
    }

    @Test
    void saveShow_noEpisodes() {
        ShowDto dto = new ShowDto(
                1L, "url", "Test Show", "Drama", "EN",
                List.of("Drama"), "Running", 60, 60,
                "2020-01-01", null, "site",
                null, new ShowDto.Rating(8.5), 1,
                null, null, null,
                null, null, "summary", 123L,
                null, null
        );

        when(restClientHelper.getForObject(anyString(), anyMap(), eq(ShowDto.class)))
                .thenReturn(dto);

        String result = showService.saveShow("Test Show");

        assertEquals("Nenhum show encontrado para : Test Show", result);
        verify(repository, never()).save(any());
    }

    @Test
    void saveShow_httpClientError() {
        when(restClientHelper.getForObject(anyString(), anyMap(), eq(ShowDto.class)))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        assertThrows(RuntimeException.class, () -> showService.saveShow("Test Show"));
    }

    @Test
    void saveShow_resourceAccessError() {
        when(restClientHelper.getForObject(anyString(), anyMap(), eq(ShowDto.class)))
                .thenThrow(ResourceAccessException.class);

        assertThrows(RuntimeException.class, () -> showService.saveShow("Test Show"));
    }

    @Test
    void saveShow_dataAccessError() {
        ShowDto dto = mock(ShowDto.class);
        when(dto._embedded()).thenReturn(new ShowDto.Embedded(List.of()));
        when(restClientHelper.getForObject(anyString(), anyMap(), eq(ShowDto.class)))
                .thenReturn(dto);
        when(repository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThrows(RuntimeException.class, () -> showService.saveShow("Test Show"));
    }
}
