package ru.d3fix4m.camera_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CameraController {

    private final CameraImageService cameraImageService;

    @GetMapping("/test")
    public void get() throws IOException {
        cameraImageService.getImages();
    }
}
