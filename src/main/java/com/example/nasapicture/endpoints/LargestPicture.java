package com.example.nasapicture.endpoints;

import com.example.nasapicture.controller.NasaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LargestPicture {

    @Autowired
    NasaController nasaController;

    @GetMapping(path = "/mars/pictures/largest")
    public ResponseEntity<byte[]> getLargestPicture (@RequestParam String sol, @RequestParam(required = false) String camera) {

        var picture = nasaController.getLargestPicture(sol, Optional.ofNullable(camera));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(picture, headers, HttpStatus.OK);

        return responseEntity;
    }
}
