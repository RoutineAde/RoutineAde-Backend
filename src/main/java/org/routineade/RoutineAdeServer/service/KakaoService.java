package org.routineade.RoutineAdeServer.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.routineade.RoutineAdeServer.dto.user.KakaoUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class KakaoService {
    private final UserService userService;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUrI;

    public String login(final String code) {
        // 카카오에서 전달해준 code 값 가져오기
        String tokenURL = "https://kauth.kakao.com/oauth/token";

        // body data 생성
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("grant_type", "authorization_code");
        parameter.add("client_id", clientId);
        parameter.add("code", code);
        parameter.add("redirect_uri", redirectUrI);

        // request header 설정
        HttpHeaders headers = new HttpHeaders();
        // Content-type을 application/x-www-form-urlencoded 로 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // header 와 body로 Request 생성
        HttpEntity<?> entity = new HttpEntity<>(parameter, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            // 응답 데이터(json)를 Map 으로 받을 수 있도록 메시지 컨버터 추가
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // Post 방식으로 Http 요청
            // 응답 데이터 형식은 Hashmap 으로 지정
            ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            // 리턴받은 access_token 가져오기
            String access_token = resMap.get("access_token");

            String userInfoURL = "https://kapi.kakao.com/v2/user/me";
            // Header에 access_token 삽입
            headers.set("Authorization", "Bearer " + access_token);

            // Request entity 생성
            HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);

            // Post 방식으로 Http 요청
            KakaoUserResponse kakaoUserResponse = restTemplate.postForEntity(userInfoURL, userInfoEntity,
                    KakaoUserResponse.class).getBody();

            String profileImage = null;

            if (kakaoUserResponse.kakaoAccount().profile() != null && !kakaoUserResponse.kakaoAccount().profile()
                    .isDefaultImage()) {
                profileImage = kakaoUserResponse.kakaoAccount().profile().profileImageUrl();
            }

            return userService.login(
                    userService.getOrCreateUser(kakaoUserResponse.kakaoAccount().email(), profileImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "토큰이 존재하지 않습니다.";
    }

}
