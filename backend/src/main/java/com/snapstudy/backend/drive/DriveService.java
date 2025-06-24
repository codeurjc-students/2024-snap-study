package com.snapstudy.backend.drive;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.auth.oauth2.GoogleCredentials;

import jakarta.mail.internet.*;
import jakarta.mail.*;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class DriveService {

    private static final Object lockObject = new Object();
    private static final String APPLICATION_NAME = "Google Drive Upload Console App";
    private static final String CREDENTIALS_FILE_PATH = System.getenv("CREDENTIALS_FILE_PATH");
    private static final String folderID = System.getenv("GOOGLE_DRIVE_FOLDER");
    private static final String MAIL_TEMPLATE_PATH = System.getenv("MAIL_TEMPLATE_PATH");

    public static Drive getDriveService() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String createFolder(String email, String folderPath, String parentFolderID) throws IOException {
        synchronized (lockObject) {

            Drive service = getDriveService();

            String[] folderNames = folderPath.split("/"); // Split the path to create each folder
            String currentFolderID = parentFolderID;

            try {
                // Intentar obtener la carpeta
                Drive.Files.Get getRequest = service.files().get(folderID);
                File folder = getRequest.execute();
                System.out.println("Folder found: " + folder.getName());
            } catch (GoogleJsonResponseException e) {
                if (e.getDetails().getCode() == 404) {
                    System.out.println("Folder not found, check the folder ID or permissions.");
                } else {
                    System.out.println("Error occurred: " + e.getDetails().getMessage());
                }
            }
            int i = 0;
            for (String folderName : folderNames) {
                // Verify if the folder already exists in the actual path
                Drive.Files.List listRequest = service.files().list()
                        .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and '"
                                + currentFolderID + "' in parents") // Look for the folder within the current folder
                        .setFields("files(id)");

                FileList existingFolders = listRequest.execute();
                List<File> files = existingFolders.getFiles();

                if (files != null && !files.isEmpty()) {
                    // If the folder already exists, we use its ID
                    currentFolderID = files.get(0).getId();
                    if (i == 0) { // parent folder
                        i++;
                        sendmail(email, currentFolderID);
                    }
                } else {
                    // If it doesnt exist, we create a new folder
                    File fileMetadata = new File();
                    fileMetadata.setName(folderName);
                    fileMetadata.setParents(Collections.singletonList(currentFolderID));
                    fileMetadata.setMimeType("application/vnd.google-apps.folder");

                    File file = service.files().create(fileMetadata)
                            .setFields("id") // We only need the folder ID
                            .execute();

                    currentFolderID = file.getId(); // We update the ID of the created folder

                    if (i == 0) { // parent folder
                        i++;
                        grantFolderPermission(currentFolderID, email);
                        sendmail(email, currentFolderID);
                    }
                }
            }
            return currentFolderID; // We return the ID of the last folder created or found
        }
    }

    public static void uploadFileToGoogleDrive(String folderID, InputStream fileStream, String fileName) {
        try {
            Drive service = getDriveService();

            if (fileExists(service, folderID, fileName)) {
                System.out.println(
                        "File '" + fileName + "' already exists in folder with ID: " + folderID + ". Skipping upload.");
                return;
            }

            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderID));

            if (fileStream == null) {
                System.out.println("El InputStream es nulo.");
                return;
            }

            AbstractInputStreamContent mediaContent = new InputStreamContent("application/octet-stream", fileStream);

            Drive.Files.Create request = service.files().create(fileMetadata, mediaContent);
            request.setFields("id");
            File uploadedFile = request.execute();

            if (uploadedFile != null) {
                System.out.println("File '" + fileMetadata.getName() + "' uploaded with ID: " + uploadedFile.getId());
            } else {
                System.out.println("File upload failed with no response body.");
            }
        } catch (Exception ex) {
            System.out.println("Error during file upload: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static boolean fileExists(Drive service, String folderID, String fileName) {
        try {
            String query = "name='" + fileName + "' and '" + folderID + "' in parents and trashed=false";
            return !service.files().list().setQ(query).setFields("files(id)").execute().getFiles().isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void UploadFileInFolder(String email, InputStream file, String filename, String folder) throws IOException {
        String newFolder = createFolder(email, folder, folderID);
        uploadFileToGoogleDrive(newFolder, file, filename);
    }

    public static void grantFolderPermission(String folderId, String email) {
        try {
            Drive service = getDriveService();

            Permission permission = new Permission();
            permission.setType("user"); // Options "user" or "group"
            permission.setRole("writer"); // Options: "reader", "writer", "commenter"
            permission.setEmailAddress(email);

            service.permissions().create(folderId, permission)
                    .setSendNotificationEmail(true) // email
                    .execute();

            System.out.println("Permiso otorgado a " + email + " en la carpeta con ID: " + folderId);

        } catch (Exception e) {
            System.err.println("Error al otorgar permisos: " + e.getMessage());
        }
    }

    public static void sendmail(String email, String folderId) throws IOException {
        final String user = System.getenv("APPLICATION_MAIL");
        final String password = System.getenv("APPLICATION_PASS_MAIL");

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", host); // SMTP Gmail server
        props.put("mail.smtp.auth", "true"); // Auth
        props.put("mail.smtp.port", "465"); // SSL
        props.put("mail.smtp.socketFactory.port", "465"); // SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL

        // Authentication with the SMTP serve
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            String htmlBody = new String(Files.readAllBytes(Paths.get(MAIL_TEMPLATE_PATH)));

            // Replace the placeholder {{folderId}} with the actual value
            htmlBody = htmlBody.replace("{{folderId}}", folderId);

            // message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Access to your folder in Google Drive");
            message.setContent(htmlBody, "text/html; charset=utf-8");

            // Send mail
            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}