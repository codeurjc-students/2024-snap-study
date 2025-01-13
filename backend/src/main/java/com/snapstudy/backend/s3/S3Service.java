package com.snapstudy.backend.s3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private static String accessKey = System.getenv("AWS_S3_ACCESS_KEY_ID");
    private static String secretAccessKey = System.getenv("AWS_S3_SECRET_ACCESS_KEY");
    private static String bucket = "snapstudy-s3";
    private static final boolean useMinIO = accessKey == null || accessKey.isEmpty() || accessKey == "change_me_access" || secretAccessKey == null || secretAccessKey.isEmpty() || secretAccessKey == "change_me_secret";

    private static MinioClient minioClient;

    static {
        if (useMinIO) {
            try {
                minioClient = MinioClient.builder()
                        .endpoint("http://127.0.0.1:9000") // Cambia a "http://192.168.18.196:9000" si es otra máquina
                        .credentials("minioadmin", "minioadmin")
                        .build();
                // Crear bucket si no existe
                boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                if (!isBucketExist) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error initializing MinIO: " + e.getMessage(), e);
            }
        }
    }

    public static String addFile(String folder, MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Create the file name
                String fileKey = folder;

                if (useMinIO) {
                    // MinIO upload
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(fileKey)
                                    .stream(file.getInputStream(), file.getSize(), -1)
                                    .build());
                    return fileKey;
                } else {

                    // AWS Credentials
                    BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

                    // Configure the S3 client
                    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                            .withRegion(Regions.EU_WEST_1)
                            .build();

                    // Create a TransferManager to handle the upload
                    TransferManager transferManager = TransferManagerBuilder.standard()
                            .withS3Client(s3Client)
                            .build();

                    // Create the upload request
                    PutObjectRequest request = new PutObjectRequest(bucket, fileKey, file.getInputStream(), null);

                    // Upload the file
                    Upload upload = transferManager.upload(request);
                    upload.waitForCompletion(); // Wait for the upload to finish

                    // Return the file name
                    return fileKey;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null; // Error uploading the file
            }
        }
        return null; // The file is empty
    }

    public static int createFolder(String folderName) {
        try {
            if (useMinIO) {
                String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";

                try {
                    minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(folderKey).build()); // Check
                                                                                                               // if the
                                                                                                               // folder
                                                                                                               // exists
                    return 1; // Folder already exists
                } catch (MinioException e) {
                    // If the folder does not exist, create it
                    ByteArrayInputStream emptyContent = new ByteArrayInputStream(new byte[0]);

                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(folderKey)
                                    .stream(emptyContent, 0, -1) // Stream with length 0
                                    .build());
                    return 0; // Folder created successfully
                }
            } else {
                // AWS Credentials
                BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);

                // Configure the S3 client
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .withRegion(Regions.EU_WEST_1)
                        .build();

                // Verify if the folder already exists
                String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";
                GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucket, folderKey);

                try {
                    // Try to get the object metadata (folder)
                    s3Client.getObjectMetadata(metadataRequest);
                    // If no exception occurs, the folder already exists
                    return 1;
                } catch (Exception e) {
                    // If an exception occurs, it means the folder does not exist, and we can create
                    // it
                    // Create an empty InputStream
                    ByteArrayInputStream emptyContent = new ByteArrayInputStream(new byte[0]);

                    // Empty metadata
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(0); // Set the content length to 0 (no content)

                    // Create the request for the "folder" in S3
                    PutObjectRequest request = new PutObjectRequest(bucket, folderKey, emptyContent, metadata);

                    // Upload the "folder" (actually an object with a key that ends with "/")
                    s3Client.putObject(request);
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2; // Error creating the folder
        }
    }

    public static int deleteFile(String folder, String fileName) {
        try {
            String key = folder + "/" + fileName;
            if (useMinIO) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucket).object(key).build());
                return 0;
            } else {
                // Configure the Amazon S3 client with our credentials
                BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(Regions.EU_WEST_1)
                        .build();

                // Delete the file by specifying the bucket and the file
                s3Client.deleteObject(new DeleteObjectRequest(bucket, key));

                return 0; // Successful deletion
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // Error deleting the file from Amazon S3
        }
    }

    public static int deleteFolder(String folderName) {
        try {
            if (useMinIO) {
                String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";

                try {
                    // Check if the folder exists
                    minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(folderKey).build());

                    // List all objects within the folder
                    Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(folderKey)
                            .build());

                    // Delete each object inside the folder
                    for (Result<Item> result : results) {
                        Item item = result.get();
                        minioClient.removeObject(
                                RemoveObjectArgs.builder().bucket(bucket).object(item.objectName()).build());
                    }
                    return 0; // Successful deletion
                } catch (Exception e) {
                    return 1; // If an exception occurs here, the folder doesn't exist
                }
            } else {
                // Configure the Amazon S3 client with our credentials
                BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(Regions.EU_WEST_1)
                        .build();

                // Check if the folder exists
                String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";

                try {
                    // If no exception is thrown, the folder exists
                    ObjectMetadata metadata = s3Client.getObjectMetadata(bucket, folderKey);

                    // Get all objects within the folder
                    ListObjectsRequest listRequest = new ListObjectsRequest()
                            .withBucketName(bucket)
                            .withPrefix(folderKey);

                    List<S3ObjectSummary> objects = s3Client.listObjects(listRequest).getObjectSummaries();

                    // Delete each object inside the folder
                    for (S3ObjectSummary object : objects) {
                        s3Client.deleteObject(new DeleteObjectRequest(bucket, object.getKey()));
                    }
                    return 0; // Successful deletion

                } catch (Exception e) {
                    return 1; // If an exception occurs here, the folder doesn't exist
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2; // Error deleting the folder from Amazon S3
        }
    }

    public Map<String, InputStream> downloadFile(String folder, String fileName) {
        Map<String, InputStream> fileMap = new HashMap<>();
        try {

            String fileKey = folder + "/" + fileName;

            if (useMinIO) {
                // MinIO download
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(fileKey)
                                .build());

                if (inputStream != null) {
                    fileMap.put(fileName, inputStream);
                }

            } else {
                // Create AWS credentials and the client
                BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(Regions.EU_WEST_1)
                        .build();

                // Create the request to retrieve the file from S3
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, fileKey);

                // Download the file from S3
                S3Object s3Object = s3Client.getObject(getObjectRequest);

                // Get the InputStream of the file
                InputStream inputStream = s3Object.getObjectContent();

                if (inputStream != null) {
                    fileMap.put(fileName, inputStream);
                }
            }
        } catch (AmazonServiceException e) {
            e.printStackTrace(); // Handle Amazon S3 exceptions
            return null; // Error downloading the file
        } catch (Exception e) {
            // Handle any other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return null; // Error downloading the file
        }
        return fileMap;
    }

}