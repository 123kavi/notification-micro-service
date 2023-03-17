package com.notification.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class FirebaseConfiguration {
    @Value(value = "${app.firebase.service-account}")
    private Resource firebaseServiceAccFilePath;
    @Value(value = "${app.firebase.project-id}")
    private String firebaseProjectId;

    @Bean(name = "firebaseMessagingClient")
    FirebaseMessaging firebaseMessagingClient() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }

    @Bean(name = "firebaseAuthClient")
    FirebaseAuth firebaseAuthClient() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }

    @Bean(name = "firebaseAppClient")
    FirebaseApp firebaseApp() throws IOException {
        return FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(getGoogleCredentials())
                .build(), firebaseProjectId);
    }

    /**
     * @return : com.google.auth.oauth2.GoogleCredentials
     * @throws java.io.IOException
     */
    private GoogleCredentials getGoogleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new ClassPathResource(
                Objects.requireNonNull(firebaseServiceAccFilePath.getFilename())).getInputStream());
    }
}
