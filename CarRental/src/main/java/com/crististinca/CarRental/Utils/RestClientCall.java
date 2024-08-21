package com.crististinca.CarRental.Utils;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Client;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

public class RestClientCall {

    public RestClientCall(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public <T> List<T> getList(Class<T> elementClass, String uri, Object... args) throws HttpClientErrorException {
        ArrayList<T> responseObjs = this.restClient.get()
                .uri(uri, args)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (responseObjs == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
        return mapper.convertValue(responseObjs, listType);
    }

    public <T> T get(Class<T> clazz, String uri, Object... args) throws HttpClientErrorException {
        ResponseEntity<T> responseObj = this.restClient.get()
                .uri(uri, args)
                .retrieve()
                .toEntity(clazz);

        return responseObj.getBody();
    }

    public <T> T post(Class<T> clazz, String uri, T obj) throws HttpClientErrorException {
        ResponseEntity<T> responseObj = this.restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(obj)
                .retrieve()
                .toEntity(clazz);

        return responseObj.getBody();
    }

    public <T> T put(Class<T> clazz, String uri, T obj) throws HttpClientErrorException {
        ResponseEntity<T> updatedObj = restClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(obj)
                .retrieve()
                .toEntity(clazz);

        return updatedObj.getBody();
    }
}
