package com.metamong.mt.global.location;

import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestTemplateKakaoLocationProvider implements LocationProvider {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String REQUEST_URI = "https://dapi.kakao.com/v2/local/search/address";
    private static final String AUTHORIZATION_PREFIX = "KakaoAK ";
    
    private final String kakaoApiKey;
    
    public RestTemplateKakaoLocationProvider(@Value("${kakao.rest-api-key}") String kakaoRestApiKey) {
        this.kakaoApiKey = kakaoRestApiKey;
    }

    @Override
    public LatLng convertAddressToLatLng(String address) throws AmbiguousAddressException {
        URI uri = UriComponentsBuilder.fromHttpUrl(REQUEST_URI)
                .queryParam("query", address)
                .encode()
                .build()
                .toUri();
        if (log.isDebugEnabled()) {
            log.debug("Request URL = {}", uri);
        }
        System.out.println(uri);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_PREFIX + this.kakaoApiKey)
                .build();
        
        String body = REST_TEMPLATE.exchange(requestEntity, String.class).getBody();
        JSONObject jsonBody = new JSONObject(body);
        JSONArray documents = jsonBody.getJSONArray("documents");
        if (documents.length() > 1) {
            throw new AmbiguousAddressException("모호한 주소 - 결과 개수: " + documents.length());
        }
        if (documents.length() < 1)  {
            throw new IllegalArgumentException("잘못된 주소 - 결과값 없음");
        }
        JSONObject addressObj = documents.getJSONObject(0).getJSONObject("address");
        return new LatLng(
                Double.parseDouble(addressObj.getString("y")),
                Double.parseDouble(addressObj.getString("x"))
        );
    }
}
