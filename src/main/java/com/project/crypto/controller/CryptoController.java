package com.project.crypto.controller;

import com.project.crypto.dto.RequestCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/crypto")
public class CryptoController {

    @Value("${crypto.url}")
    private String baseUrl;

    @PostMapping
    public ResponseEntity<Object> postCrypto(
            @RequestBody RequestCrypto requestCrypto
    ){
        String url = baseUrl + "crypto";
        RestTemplate restTemplate = new RestTemplate();

        Object result = restTemplate.postForObject(url, requestCrypto, Object.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Object[]> listCrypto(){
        String url = baseUrl + "crypto";
        RestTemplate restTemplate = new RestTemplate();

        Object[] result = restTemplate.getForObject(url, Object[].class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detailCrypto(@PathVariable Integer id){
        String url = baseUrl + "crypto/" + id;
        RestTemplate restTemplate = new RestTemplate();

        Object result = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Object> updateCrypto(@PathVariable Integer id, @RequestBody RequestCrypto request){
        String url = baseUrl + "crypto/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<RequestCrypto> entity = new HttpEntity<RequestCrypto>(request,headers);
        RestTemplate restTemplate = new RestTemplate();

//        Object result = restTemplate.put(url, request, Object.class, Map.of("id"));
        Object result = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class).getBody();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCrypto(@PathVariable Integer id){
        String url = baseUrl + "crypto/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<RequestCrypto> entity = new HttpEntity<RequestCrypto>(headers);
        RestTemplate restTemplate = new RestTemplate();

//        Object result = restTemplate.put(url, request, Object.class, Map.of("id"));
        Object result = restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class).getBody();
        return ResponseEntity.ok(result);
    }
}
