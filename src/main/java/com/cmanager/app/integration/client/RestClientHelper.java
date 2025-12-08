package com.cmanager.app.integration.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class RestClientHelper {

    private final RestTemplate restTemplate;

    public RestClientHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T getForObject(String baseUrl, Map<String, String> queryParams, Class<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        queryParams.forEach(builder::queryParam);

        String url = builder.toUriString();
        return restTemplate.getForObject(url, responseType);
    }
}
