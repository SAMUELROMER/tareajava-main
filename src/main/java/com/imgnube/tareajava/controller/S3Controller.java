package com.imgnube.tareajava.controller;

import com.imgnube.tareajava.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = s3Service.uploadFile(file);
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"La imagen se subio correctamente!\", \"data\": {\"url\": \"" + url + "\"}}");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"No se pudo subir la imagen\", \"data\": null}");
        }
    }
}
