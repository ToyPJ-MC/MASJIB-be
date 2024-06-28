package Backend.MASJIB.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class StorgaeConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        Resource resource = new ClassPathResource(location);
        InputStream inputStream = resource.getInputStream();

        // Read JSON key file and create GoogleCredentials
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

        // Build StorageOptions with the credentials and project ID
        StorageOptions options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)  // Replace with your project ID
                .build();

        // Create and return Storage service
        Storage storage = options.getService();
        return storage;
    }
}
