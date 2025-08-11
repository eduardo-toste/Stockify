package com.eduardo.stockify.aws.secrets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class JwtSecretConfig {

    @Bean(name = "jwtSecret")
    @Profile("!prod")
    public String devJwtSecret(
            @Value("${security.jwt.secret:12345678}") String secret) {
        return secret;
    }

    @Bean(name = "jwtSecret")
    @Profile("prod")
    public String prodJwtSecret(SecretsManagerService secretsManagerService) {
        return secretsManagerService.getSimpleSecret("stockify-jwt");
    }
}
