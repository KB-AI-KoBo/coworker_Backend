package com.kobo.coworker.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("coworker API")
                .description("중소기업의 재무활동 분석을 통한 맞춤형 서비스를 제안하는 RAG 기반 챗봇 서비스 Parker API 명세서")
                .version("1.0.0");

        String jwtSchemeName = "Authorization";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addServersItem(new Server().url("https://localhost:5050"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public org.springdoc.core.models.GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .build();
    }
}

