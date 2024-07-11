package com.imgnube.tareajava.controller;

import com.imgnube.tareajava.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Recibida solicitud para subir archivo: {}", file.getOriginalFilename());
        logger.debug("Tamaño del archivo: {} bytes", file.getSize());
        logger.debug("Tipo de contenido: {}", file.getContentType());

        if (file.isEmpty()) {
            logger.warn("Se intentó subir un archivo vacío");
            return ResponseEntity.badRequest().body("No se puede subir un archivo vacío");
        }

        try {
            logger.debug("Iniciando proceso de subida con S3Service");
            String fileUrl = s3Service.uploadFile(file);
            logger.info("Archivo subido correctamente. URL: {}", fileUrl);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            logger.error("Error al subir archivo: {}", file.getOriginalFilename(), e);
            logger.debug("Detalles del error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al subir archivo: " + e.getMessage());
        } finally {
            logger.debug("Finalizando procesamiento de solicitud de subida");
        }
    }

    // Puedes agregar más métodos aquí si es necesario

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Error no manejado en el controlador", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Se produjo un error inesperado: " + e.getMessage());
    }
}