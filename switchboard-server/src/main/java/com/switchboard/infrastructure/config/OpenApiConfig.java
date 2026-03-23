package com.switchboard.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI switchboardOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Switchboard API")
                .description("Feature flag control plane")
                .version("0.1.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearer"))
            .schemaRequirement("bearer", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .description("API key or SDK key"));
    }
}
