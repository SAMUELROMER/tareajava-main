package com.imgnube.tareajava.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Date;


@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private String bucketName = "s3-prenube";

    public String uploadFile(MultipartFile file) throws IOException, SchedulerException {
        String fileName = file.getOriginalFilename();
        String contentType = "image/jpeg";

        logger.info("Iniciando subida de archivo: {}", fileName);
        // ... (resto del código de subida)

        // Programar la eliminación del archivo
        scheduleFileDeletion(fileName);

        String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        logger.info("URL del archivo subido: {}. Se eliminará en 1 minuto.", fileUrl);
        return fileUrl;
    }

    private void scheduleFileDeletion(String fileName) throws SchedulerException {
        logger.info("Programando eliminación del archivo: {}", fileName);
        
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail job = JobBuilder.newJob(FileDeleteJob.class)
                .withIdentity(fileName, "deleteGroup")
                .usingJobData("fileName", fileName)
                .usingJobData("bucketName", bucketName)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(fileName + "Trigger", "deleteGroup")
                .startAt(new Date(System.currentTimeMillis() + 60000)) // 1 minuto
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    // Job para eliminar el archivo
    public static class FileDeleteJob implements Job {
        @Autowired
        private S3Client s3Client;

        private static final Logger logger = LoggerFactory.getLogger(FileDeleteJob.class);

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String fileName = dataMap.getString("fileName");
            String bucketName = dataMap.getString("bucketName");

            logger.info("Ejecutando tarea de eliminación para el archivo: {}", fileName);

            try {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();
                s3Client.deleteObject(deleteObjectRequest);
                logger.info("Archivo eliminado con éxito: {}", fileName);
            } catch (Exception e) {
                logger.error("Error al eliminar el archivo: {}", fileName, e);
            }
        }
    }
}