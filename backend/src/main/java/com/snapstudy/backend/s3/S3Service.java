package com.snapstudy.backend.s3;

import java.io.ByteArrayInputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private static String accessKey = System.getenv("AWS_S3_ACCESS_KEY_ID");
    private static String secretAccessKey = System.getenv("AWS_S3_SECRET_ACCESS_KEY");
    private static String serviceUrl = "https://s3.eu-west-1.amazonaws.com";
    private static String bucket = "snapstudy-s3";

    public static String addFile(String folder, MultipartFile file) {
        if (!file.isEmpty()) {
            try {

                // Credenciales de AWS
                BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

                // Configuración del cliente de S3
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.EU_WEST_1)
                .build();

                // Crear un TransferManager para manejar la subida
                TransferManager transferManager = TransferManagerBuilder.standard()
                        .withS3Client(s3Client)
                        .build();

                // Crear el nombre del archivo con el timestamp
                String fileKey = folder;

                // Crear la solicitud de subida
                PutObjectRequest request = new PutObjectRequest(bucket, fileKey, file.getInputStream(), null);
                
                // Subir el archivo
                Upload upload = transferManager.upload(request);
                upload.waitForCompletion();  // Esperar a que la subida termine

                // Devolver el nombre del archivo
                return fileKey;
            } catch (Exception e) {
                e.printStackTrace();
                return null; // Error al subir el archivo
            }
        }
        return null; // El archivo está vacío
    }


    public static int createFolder(String folderName) {
        try {
            // Credenciales de AWS
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

            // Configuración del cliente de S3
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration(serviceUrl, ""))
                    .build();

            // Verificar si la carpeta ya existe
            String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";
            GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucket, folderKey);

            try {
                // Intentar obtener los metadatos del objeto (carpeta)
                s3Client.getObjectMetadata(metadataRequest);
                // Si no se produce una excepción, la carpeta ya existe
                return 1; // Carpeta ya existe
            } catch (Exception e) {
                // Si se produce una excepción, significa que la carpeta no existe y podemos crearla
                // Crear un InputStream vacío
                ByteArrayInputStream emptyContent = new ByteArrayInputStream(new byte[0]);

                // Metadatos vacíos
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(0); // Longitud del contenido es 0

                // Crear la solicitud para la "carpeta" en S3
                PutObjectRequest request = new PutObjectRequest(bucket, folderKey, emptyContent, metadata);

                // Subir la "carpeta" (realmente un objeto con una clave que termina en "/")
                s3Client.putObject(request);
                return 0; // Carpeta creada
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2; // Error al crear la carpeta
        }
    }

}