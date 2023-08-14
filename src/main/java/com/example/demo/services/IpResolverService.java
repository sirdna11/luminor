package com.example.demo.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class IpResolverService {

    private final RestTemplate restTemplate;

    @Autowired
    public IpResolverService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<String> resolveCountry(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip + "?fields=countryCode";
            IpApiResponse response = restTemplate.getForObject(url, IpApiResponse.class);
            return Optional.ofNullable(response.getCountryCode());
        } catch (Exception e) {
            // If resolution fails or any other error occurs
            return Optional.empty();
        }
    }

    @Data // Using Lombok for getter, setter
    private static class IpApiResponse {
        private String countryCode;
    }
}
