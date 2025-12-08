package com.cmanager.app.application.service;

import com.cmanager.app.application.data.EpisodesPageResponse;
import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.domain.Episodes;
import com.cmanager.app.application.mapper.Mapper;
import com.cmanager.app.application.repository.EpisodesRepository;
import com.cmanager.app.application.repository.ShowRepository;
import com.cmanager.app.application.specification.EpisodesSpecification;
import com.cmanager.app.integration.client.RestClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class EpisodesService {
    private static final Logger log = LoggerFactory.getLogger(EpisodesService.class);
    private RestTemplate restTemplate;
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowService showService;
    @Autowired
    private RestClientHelper restClientHelper;

    @Autowired
    private EpisodesRepository episodesRepository;

    public String saveEpisodes(String name) {
        try {
            ShowDto dto = showService.findShow(name);

            if (dto == null) {
                throw new IllegalStateException("API retornou resposta nula para o show: " + name);
            }

            int savedCount = 0;

            if (dto._embedded() != null && dto._embedded().episodes() != null) {
                var episodes = Mapper.toEntityList(dto._embedded().episodes());
                episodesRepository.saveAll(episodes);
                savedCount = episodes.size();
            } else {
                log.info("Nenhum episódio encontrado para o show: " + name);
                return "Nenhum episódio encontrado para o show: " + name;
            }

            return "Foram cadastrados " + savedCount + " novos episódios";

        } catch (HttpClientErrorException e) {
            log.error("Erro ao buscar show '{}': {} - {}", name, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Erro na requisição: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            log.error("Falha de acesso à API para show '{}'", name, e);
            throw new RuntimeException("Não foi possível acessar a API de TVMaze", e);
        } catch (DataAccessException e) {
            log.error("Erro ao salvar episódios no banco '{}'", name, e);
            throw new RuntimeException("Erro ao salvar episódios no banco", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar show '{}'", name, e);
            throw new RuntimeException("Erro inesperado ao buscar show: " + name, e);
        }
    }
    public EpisodesPageResponse searchEpisodes(
            String name, Integer season, String type, Integer runtime,
            int page, int size) {

        Specification<Episodes> spec = Specification.allOf(
                EpisodesSpecification.hasName(name),
                EpisodesSpecification.hasSeason(season),
                EpisodesSpecification.hasType(type),
                EpisodesSpecification.hasRuntimeGreaterThan(runtime)
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Episodes> result = episodesRepository.findAll(spec, pageable);

        double soma = result.getContent().stream()
                .mapToDouble(ep -> ep.getRating() != null && ep.getRating().getAverage() != null
                        ? ep.getRating().getAverage()
                        : 0.0)
                .sum();

        double media = result.getContent().isEmpty()
                ? 0.0
                : soma / result.getContent().size();

        EpisodesPageResponse response = new EpisodesPageResponse();
        response.setEpisodes(result.getContent());
        response.setMedia(media);
        response.setTotalElements((int) result.getTotalElements());
        response.setTotalPages(result.getTotalPages());

        return response;
    }

}
