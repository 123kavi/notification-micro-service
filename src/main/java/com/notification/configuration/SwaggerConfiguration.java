package com.notification.configuration;

import com.notification.util.Utility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.Collections;

@Configuration
public class SwaggerConfiguration {

    private static final String SWAGGER_AUTH_DESCRIPTION = "accessEverything";
    @Value(value = "${app.swagger.title}")
    private String title;
    @Value(value = "${app.swagger.description}")
    private String description;
    @Value(value = "${app.swagger.version}")
    private String version;
    @Value(value = "${app.swagger.terms-of-service.url}")
    private String termsOfServiceUrl;
    @Value(value = "${app.swagger.contact.name}")
    private String contactName;
    @Value(value = "${app.swagger.contact.url}")
    private String contactUrl;
    @Value(value = "${app.swagger.contact.email}")
    private String contactEmail;
    @Value(value = "${app.swagger.license}")
    private String license;
    @Value(value = "${app.swagger.license-url}")
    private String licenseUrl;

    /**
     * @return springfox.documentation.service.ApiInfo
     * Swagger API Info Details
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(title, description, version, termsOfServiceUrl,
                new Contact(contactName, contactUrl, contactEmail), license,
                licenseUrl, Collections.emptyList());
    }

    /**
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * @return springfox.documentation.service.ApiKey
     */
    private ApiKey apiKey() {
        return new ApiKey(Utility.AUTHORIZATION_HEADER, Utility.SWAGGER_API_KEY_NAME_JWT, Utility.HEADER);
    }

    /**
     * @return springfox.documentation.spi.service.contexts.SecurityContext
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    /**
     * @return java.util.List<springfox.documentation.service.SecurityReference>
     * Defines Default Auth Details
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(Utility.GLOBAL_SCOPE, SWAGGER_AUTH_DESCRIPTION);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference(Utility.AUTHORIZATION_HEADER, authorizationScopes));
    }
}