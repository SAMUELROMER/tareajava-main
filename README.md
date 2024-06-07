# S3 Demo Application

Este proyecto es una aplicación de demostración que muestra cómo subir archivos a un bucket de AWS S3 usando Spring Boot.

## Requisitos

- Java 17
- Maven
- AWS S3 Bucket y credenciales de IAM

## Configuración

1. Clonar el repositorio.
2. Configurar las credenciales de AWS en `src/main/resources/application.properties`.
3. Ejecutar el comando `mvn spring-boot:run` para iniciar la aplicación.

## Uso

1. Acceder a `http://localhost:8080` en el navegador.
2. Seleccionar un archivo de imagen y hacer clic en "Subir".
3. La imagen se subirá a S3 y se mostrará en la página.

## Dependencias

- Spring Boot
- AWS SDK para Java
- Lombok
