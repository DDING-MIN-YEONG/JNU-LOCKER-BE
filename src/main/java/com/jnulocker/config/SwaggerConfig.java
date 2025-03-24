package com.jnulocker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    private static final String SCHEME = "bearer"; // basic, bearer, oauth2
    private static final String BEARER_FORMAT = "JWT";
    private static final String SECURITY_SCHEME_NAME = "access-token";
    private static final String PROJECT_NAME = "JNU-Locker";
    private static final String PROJECT_URL = "https://github.com/DDING-MIN-YEONG/JNU-LOCKER-BE";
    private static final String DOCUMENT_VERSION = "v0.0.1";

    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {

        String contextPath = servletContext.getContextPath();
        Server server = new Server().url(contextPath);

        // JWT 토큰을 위한 SecurityScheme 정의
        SecurityScheme securityScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(SCHEME)
                        .bearerFormat(BEARER_FORMAT)
                        .in(SecurityScheme.In.HEADER)
                        .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement securityRequirement =
                new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .servers(List.of(server))
                .info(swaggerInfo())
                .components(
                        new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(securityRequirement);
    }

    private Info swaggerInfo() {
        License license = new License();
        license.setUrl(PROJECT_URL);
        license.setName(PROJECT_NAME);

        return new Info()
                .version(DOCUMENT_VERSION)
                .title(PROJECT_NAME + " API문서")
                .description(PROJECT_NAME + "의 API 문서 입니다.")
                .license(license);
    }
}
