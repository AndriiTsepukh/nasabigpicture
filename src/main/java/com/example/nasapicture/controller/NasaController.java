package com.example.nasapicture.controller;

import com.example.nasapicture.entity.Photos;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@Component
public class NasaController {

    final String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    RestTemplate restTemplate = new RestTemplate();

    public byte[] getLargestPicture(String sol, Optional<String> camera) {
        var composedUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("sol", sol)
                .queryParam("api_key", "DEMO_KEY")
                .queryParamIfPresent("camera", camera)
                .build().toUri();
        var photos = restTemplate.getForEntity(composedUrl, Photos.class);

        var imgSrc = photos.getBody().getPhotos().stream().map(photo -> photo.getImg_src()).toList();

        System.out.println("Pictures qty: " + imgSrc.size());

        Map<String, Long> resultMap = new HashMap<>();

        for (var imgUrl : imgSrc) {
            var length = getPictureSize(imgUrl);
            resultMap.put(imgUrl, length);
        }

        var biggestPicture = resultMap.entrySet().stream().max((pict1, pict2) -> {
            if (pict1.getValue().equals(pict2.getValue())) return 0;
            return ((pict1.getValue() - pict2.getValue()) > 0) ? 1 : -1;
        }).orElseThrow(() -> new RuntimeException("No picture found"));

        System.out.println(resultMap);

        var response = getPicture(biggestPicture.getKey());

        return response;
    }

    private long getPictureSize(String url) {
        long size;
        var response = restTemplate.getForEntity(url, byte[].class);
        var responseCode = response.getStatusCode();
        if (responseCode.equals(MOVED_PERMANENTLY)) {
            var location = response.getHeaders().getLocation();
            size = getPictureSize(location.toString());
        } else {
            size = response.getHeaders().getContentLength();
        }
        return size;
    }

    private byte[] getPicture(String url) {
        byte[] picture;
        var response = restTemplate.getForEntity(url, byte[].class);
        var responseCode = response.getStatusCode();
        if (responseCode.equals(MOVED_PERMANENTLY)) {
            var location = response.getHeaders().getLocation();
            picture = getPicture(location.toString());
        } else {
            picture = response.getBody();
        }
        return picture;
    }
}
