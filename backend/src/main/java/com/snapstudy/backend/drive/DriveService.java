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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DriveService {

    private static final Object lockObject = new Object();
    private static final String APPLICATION_NAME = "Google Drive Upload Console App";
    private static final String CREDENTIALS_FILE_PATH = "../credentials.json";
    private static final String folderID = "1CMnbMWN9vg4f25GyFCFHXVDBIVwQQWUl";

    public static Drive getDriveService() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String createFolder(String folderPath, String parentFolderID) throws IOException {
        synchronized (lockObject) {

            Drive service = getDriveService();

            String[] folderNames = folderPath.split("/"); // Divide el path para crear cada carpeta
            String currentFolderID = parentFolderID;

            try {
                // Intentar obtener la carpeta
                Drive.Files.Get getRequest = service.files().get("1CMnbMWN9vg4f25GyFCFHXVDBIVwQQWUl");
                File folder = getRequest.execute();
                System.out.println("Folder found: " + folder.getName());
            } catch (GoogleJsonResponseException e) {
                if (e.getDetails().getCode() == 404) {
                    System.out.println("Folder not found, check the folder ID or permissions.");
                } else {
                    System.out.println("Error occurred: " + e.getDetails().getMessage());
                }
            }

            for (String folderName : folderNames) {
                // Verificar si la carpeta ya existe en el directorio actual
                Drive.Files.List listRequest = service.files().list()
                        .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and '"
                                + currentFolderID + "' in parents") // Busca la carpeta dentro de la carpeta actual
                        .setFields("files(id)");

                FileList existingFolders = listRequest.execute();
                List<File> files = existingFolders.getFiles();

                if (files != null && !files.isEmpty()) {
                    // Si la carpeta ya existe, usamos su ID
                    currentFolderID = files.get(0).getId();
                } else {
                    // Si no existe, creamos una nueva carpeta
                    File fileMetadata = new File();
                    fileMetadata.setName(folderName);
                    fileMetadata.setParents(Collections.singletonList(currentFolderID));
                    fileMetadata.setMimeType("application/vnd.google-apps.folder");

                    File file = service.files().create(fileMetadata)
                            .setFields("id") // Solo necesitamos el ID de la carpeta
                            .execute();

                    currentFolderID = file.getId(); // Actualizamos el ID de la carpeta creada
                }
            }
            return currentFolderID; // Devolvemos el ID de la última carpeta creada o encontrada
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

    public void UploadFileInFolder(InputStream file, String filename, String folder) throws IOException {
        String newFolder = createFolder(folder, folderID);
        uploadFileToGoogleDrive(newFolder, file, filename);
    }

}