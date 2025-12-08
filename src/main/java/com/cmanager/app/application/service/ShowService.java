package com.cmanager.app.application.service;

import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.mapper.Mapper;
import com.cmanager.app.application.repository.ShowRepository;
import com.cmanager.app.integration.client.RestClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@Service
public class ShowService {

    private static final Logger log = LoggerFactory.getLogger(ShowService.class);

    @Autowired
    private ShowRepository repository;
    @Autowired
    private RestClientHelper restClientHelper;

    public String saveShow(String name) {
        try {
            ShowDto dto = findShow(name);

            if (dto == null) {
                throw new IllegalStateException("API retornou resposta nula para o show: " + name);
            }

            if (dto != null && dto._embedded() != null) {
                var show = Mapper.toEntity(dto);
                repository.save(show);

            } else {
                log.info("Nenhum show encontrado para : " + name);
                return "Nenhum show encontrado para : " + name;
            }

            return "Show cadastrado com sucesso.";

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

    public ShowDto findShow(String name) {
        Map<String, String> params = Map.of(
                "q", name,
                "embed", "episodes"
        );

        try {
            ShowDto dto = restClientHelper.getForObject(
                    "https://api.tvmaze.com/singlesearch/shows",
                    params,
                    ShowDto.class
            );

            if (dto == null) {
                log.warn("API retornou resposta nula para o show: {}", name);
                throw new IllegalStateException("API retornou resposta nula para o show: " + name);
            }

            log.info("Show '{}' encontrado com {} episódios", dto.name(),
                    dto._embedded() != null && dto._embedded().episodes() != null
                            ? dto._embedded().episodes().size()
                            : 0);
            return dto;

        } catch (HttpClientErrorException e) {
            log.error("Erro ao buscar show '{}': {} - {}", name, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Erro na requisição: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            log.error("Falha de acesso à API para show '{}'", name, e);
            throw new RuntimeException("Não foi possível acessar a API de TVMaze", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar show '{}'", name, e);
            throw new RuntimeException("Erro inesperado ao buscar show: " + name, e);
        }
    }
}
