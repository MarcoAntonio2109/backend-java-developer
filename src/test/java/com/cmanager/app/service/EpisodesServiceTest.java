package com.cmanager.app.service;

import com.cmanager.app.application.data.EpisodesPageResponse;
import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.domain.Episodes;
import com.cmanager.app.application.repository.EpisodesRepository;
import com.cmanager.app.application.repository.ShowRepository;
import com.cmanager.app.application.service.EpisodesService;
import com.cmanager.app.application.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EpisodesServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowService showService;

    @Mock
    private EpisodesRepository episodesRepository;

    @InjectMocks
    private EpisodesService episodesService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveEpisodes_success() {

        ShowDto.Embedded.Episode episode = new ShowDto.Embedded.Episode(
                1L, "url", "Pilot", 1, 1, "Drama", "2020-01-01",
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
                null, new ShowDto.Rating(8.5), 1,
                null, null, null,
                null, null, "summary", 123L,
                null, new ShowDto.Embedded(List.of(episode))
        );

        when(showService.findShow("Test Show")).thenReturn(dto);
        String result = episodesService.saveEpisodes("Test Show");

        assertEquals("Foram cadastrados 1 novos episódios", result);
        verify(episodesRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveEpisodes_noEpisodes() {
        ShowDto dto = new ShowDto(
                1L, "url", "Test Show", "Drama", "EN",
                List.of("Drama"), "Running", 60, 60,
                "2020-01-01", null, "site",
                null, new ShowDto.Rating(8.5), 1,
                null, null, null,
                null, null, "summary", 123L,
                null, new ShowDto.Embedded(null)
        );

        when(showService.findShow("Test Show")).thenReturn(dto);

        String result = episodesService.saveEpisodes("Test Show");

        assertEquals("Foram cadastrados 0 novos episódios", result);
        verify(episodesRepository, never()).saveAll(anyList());
    }

    @Test
    void saveEpisodes_httpClientError() {
        when(showService.findShow("Test Show"))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        assertThrows(RuntimeException.class, () -> episodesService.saveEpisodes("Test Show"));
    }

    @Test
    void saveEpisodes_resourceAccessError() {
        when(showService.findShow("Test Show"))
                .thenThrow(ResourceAccessException.class);

        assertThrows(RuntimeException.class, () -> episodesService.saveEpisodes("Test Show"));
    }

    @Test
    void saveEpisodes_dataAccessError() {
        ShowDto dto = mock(ShowDto.class);
        when(dto._embedded()).thenReturn(new ShowDto.Embedded(List.of()));
        when(showService.findShow("Test Show")).thenReturn(dto);
        doThrow(mock(DataAccessException.class)).when(episodesRepository).saveAll(anyList());

        assertThrows(RuntimeException.class, () -> episodesService.saveEpisodes("Test Show"));
    }

    @Test
    void searchEpisodes_calculaMediaCorretamente() {
        Episodes ep1 = new Episodes();
        ep1.setName("Ep1");
        ep1.setRating(new com.cmanager.app.application.domain.Rating());
        ep1.getRating().setAverage(8.0);

        Episodes ep2 = new Episodes();
        ep2.setName("Ep2");
        ep2.setRating(new com.cmanager.app.application.domain.Rating());
        ep2.getRating().setAverage(null); // deve ser considerado 0

        Page<Episodes> page = new PageImpl<>(List.of(ep1, ep2), PageRequest.of(0, 10), 2);

        when(episodesRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        EpisodesPageResponse response = episodesService.searchEpisodes("Test", 1, "Drama", 60, 0, 10);

        assertEquals(2, response.getEpisodes().size());
        assertEquals(4.0, response.getMedia()); // (8 + 0) / 2
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
    }

    @Test
    void searchEpisodes_listaVaziaMediaZero() {
        Page<Episodes> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        when(episodesRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        EpisodesPageResponse response = episodesService.searchEpisodes("Test", null, null, null, 0, 10);

        assertEquals(0.0, response.getMedia());
        assertEquals(0, response.getEpisodes().size());
    }
}
