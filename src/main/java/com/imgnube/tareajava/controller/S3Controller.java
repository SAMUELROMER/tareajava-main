package com.imgnube.tareajava.controller;

// Importaciones necesarias de Spring y Lombok
import com.imgnube.tareajava.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController // Indica que esta clase es un controlador REST de Spring
@RequestMapping("/api/s3") // Mapea las solicitudes HTTP a /api/s3
@RequiredArgsConstructor // Genera un constructor con todos los campos finales como parámetros
public class S3Controller {

    // Inyección del servicio S3Service
    private final S3Service s3Service;

    @PostMapping("/upload") // Mapea las solicitudes POST a /api/s3/upload
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Llama al método uploadFile del servicio S3Service y obtiene la URL del archivo subido
            String url = s3Service.uploadFile(file);
            // Devuelve una respuesta HTTP 200 OK con un mensaje de éxito y la URL del archivo subido
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"La imagen se subió correctamente!\", \"data\": {\"url\": \"" + url + "\"}}");
        } catch (IOException e) {
            // Devuelve una respuesta HTTP 500 Internal Server Error en caso de excepción
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"No se pudo subir la imagen\", \"data\": null}");
        }
    }
}
