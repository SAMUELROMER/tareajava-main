package com.imgnube.tareajava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Service // Indica que esta clase es un servicio gestionado por Spring
@RequiredArgsConstructor // Genera un constructor con todos los campos finales como parámetros
public class S3Service {

    // Inyección de dependencias: Cliente S3 y S3Presigner
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    // Inyección del nombre del bucket desde las propiedades de configuración
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    // Método para subir un archivo a S3
    public String uploadFile(MultipartFile file) throws IOException {
        // Genera una clave única para el archivo utilizando el tiempo actual y el nombre original del archivo
        String key = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        
        // Crea una solicitud para subir el archivo
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // Especifica el bucket
                .key(key) // Especifica la clave del archivo
                .contentType(file.getContentType()) // Especifica el tipo de contenido del archivo
                .build();

        // Sube el archivo a S3 utilizando la solicitud y el cuerpo del archivo
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Crea una solicitud para obtener una URL pre-firmada para el archivo subido
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(r -> r.bucket(bucketName).key(key)) // Especifica el bucket y la clave del archivo
                .signatureDuration(Duration.ofHours(1)) // Especifica la duración de la validez de la URL pre-firmada
                .build();

        // Genera la URL pre-firmada utilizando la solicitud
        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        
        // Devuelve la URL pre-firmada como cadena
        return presignedGetObjectRequest.url().toString();
    }
}
