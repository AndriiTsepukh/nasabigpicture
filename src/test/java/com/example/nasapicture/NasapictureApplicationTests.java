package com.example.nasapicture;

import com.example.nasapicture.controller.NasaController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class NasapictureApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void nasaControllerTest() {
        NasaController nasaController = new NasaController();

        nasaController.getLargestPicture("1000", Optional.of("NAVCAM"));
    }


}
