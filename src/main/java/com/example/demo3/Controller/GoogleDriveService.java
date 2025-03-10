package com.example.demo3.Controller;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.drive.DriveScopes;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class GoogleDriveService {
    private static Drive getDriveService() throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream("path/to/service_account.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                .createScoped(Collections.singletonList(DriveScopes.DRIVE));
        return new Drive.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Google Drive Integration")
                .build();
    }

    public static void createFolder(String folderName) {
        try {
            Drive service = getDriveService();
            File fileMetadata = new File();
            fileMetadata.setName(folderName);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            File folder = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder created: " + folder.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

