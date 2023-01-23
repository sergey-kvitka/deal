package com.kvitka.deal.services.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <I, O> ResponseEntity<O> postForEntity(String url, I requestBody, Class<O> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<I> httpEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, httpEntity, responseType);
    }
}
