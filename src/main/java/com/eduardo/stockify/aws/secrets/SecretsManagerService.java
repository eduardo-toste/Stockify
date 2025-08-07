package com.eduardo.stockify.aws.secrets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.util.Map;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SecretsManagerService() {
        this.client = SecretsManagerClient.builder()
                .region(Region.of("us-east-1"))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public Map<String, String> getSecret(String secretName) {
        var request = GetSecretValueRequest.builder().secretId(secretName).build();
        var secretValue = client.getSecretValue(request);
        try {
            return objectMapper.readValue(secretValue.secretString(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear segredo", e);
        }
    }

    public String getSimpleSecret(String secretName) {
        var request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        var secretValue = client.getSecretValue(request);
        return secretValue.secretString();
    }
}