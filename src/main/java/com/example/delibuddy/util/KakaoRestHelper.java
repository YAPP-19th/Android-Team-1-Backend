package com.example.delibuddy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class KakaoRestHelper {

    public static KakaoMyInfo getKakaoMyInfo(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", request, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(stringResponseEntity.getBody());
        return new KakaoMyInfo(
            jsonNode.get("id").asText(),
            jsonNode.get("kakao_account") != null ? jsonNode.get("kakao_account").get("email").asText() : null,
            jsonNode.get("properties") !=null ? jsonNode.get("properties").get("profile_image").asText() : null,
            jsonNode.get("properties") !=null ? jsonNode.get("properties").get("nickname").asText() : null
        );
    }
}
