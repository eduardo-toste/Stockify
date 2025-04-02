package com.eduardo.stockify.config.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stockify API")
                        .description("API Rest da aplicação Sctockify, para gerenciamento de estoque")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("backend@stockify.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://stockify/api/licenca")));
    }

}
