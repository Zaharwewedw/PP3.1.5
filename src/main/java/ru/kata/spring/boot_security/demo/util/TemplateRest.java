package ru.kata.spring.boot_security.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class TemplateRest {

    public static void main(String[] args) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClients.custom()
                .setDefaultCookieStore(new BasicCookieStore()).build()));

        String url = "http://94.198.50.185:7081/api/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);

        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        String sessionId = cookies.get(0);

        headers.set(HttpHeaders.COOKIE, sessionId);

        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte) 22);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String userJson = objectMapper.writeValueAsString(user);

        HttpEntity<String> requestBody = new HttpEntity<>(userJson, headers);
        ResponseEntity<String> responseEntityPost = restTemplate.exchange(url, HttpMethod.POST, requestBody, String.class);

        System.out.println(responseEntityPost.getBody());

        User userPut = new User();
        userPut.setId(3L);
        userPut.setName("Thomas");
        userPut.setLastName("Shelby");
        userPut.setAge((byte) 22);

        ObjectMapper objectMapperPut = new ObjectMapper();
        objectMapperPut.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String userJsonPut = objectMapper.writeValueAsString(userPut);

        HttpEntity<String> requestEntityPut = new HttpEntity<>(userJsonPut, headers);
        ResponseEntity<String> responseEntityPut = restTemplate.exchange(url, HttpMethod.PUT, requestEntityPut, String.class);

        System.out.println(responseEntityPut.getBody());

        url = "http://94.198.50.185:7081/api/users/3";

        HttpEntity<?> requestEntityDel = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntityDel = restTemplate.exchange(url, HttpMethod.DELETE, requestEntityDel, String.class);

        System.out.println(responseEntityDel.getBody());
    }

}
