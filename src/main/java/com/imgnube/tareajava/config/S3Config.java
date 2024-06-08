package com.imgnube.tareajava.config;

// Importaciones necesarias de Spring y AWS SDK
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration // Indica que esta clase es una fuente de configuración de beans de Spring
public class S3Config {

    // Inyección de valores desde el archivo de configuración (application.properties o application.yml)
    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Bean // Define este método como un bean gestionado por Spring
    public S3Client s3Client() {
        // Crear credenciales básicas de AWS utilizando los valores inyectados
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        // Construir y devolver una instancia del cliente S3 configurado con la región y las credenciales
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    @Bean // Define este método como un bean gestionado por Spring
    public S3Presigner s3Presigner() {
        // Crear credenciales básicas de AWS utilizando los valores inyectados
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        // Construir y devolver una instancia de S3Presigner configurado con la región y las credenciales
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}
