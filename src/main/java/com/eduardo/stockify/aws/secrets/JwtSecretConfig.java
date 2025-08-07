package com.eduardo.stockify.aws.secrets;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class JwtSecretConfig {

    private final SecretsManagerService secretsManagerService;

    @Bean
    public String jwtSecret() {
        return secretsManagerService.getSimpleSecret("stockify-jwt");
    }

}
