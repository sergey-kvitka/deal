package com.kvitka.deal.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <I, O> ResponseEntity<O> postForEntity(String url, I requestBody, Class<O> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<I> httpEntity = new HttpEntity<>(requestBody, headers);
        log.info("POST request is about to be sent with values: (URL = {}, HTTP entity = {})", url, httpEntity);
        ResponseEntity<O> responseEntity = restTemplate.postForEntity(url, httpEntity, responseType);
        log.info("POST request sent and response entity received (response entity = {})", responseEntity);
        return responseEntity;
    }
}
