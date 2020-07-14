package com.vodafone.devicetracker;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vodafone.devicetracker.util.StringConstants;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Base application class for starting the application.
 */
@SpringBootApplication
@EnableSwagger2
public class DeviceLocationApplication {

    @Value("${swagger.title}")
    private String swaggerTitle;
    @Value("${swagger.description}")
    private String swaggerDescription;
    @Value("${swagger.version}")
    private String swaggerVersion;
    @Value("${swagger.termsOfServiceUrl}")
    private String swaggerTermsOfServiceUrl;
    @Value("${swagger.contact}")
    private String swaggerContact;
    @Value("${swagger.contactEmail}")
    private String swaggerContactEmail;
    @Value("${swagger.license}")
    private String swaggerLicense;
    @Value("${swagger.licenseUrl}")
    private String swaggerLicenseUrl;

    public static void main(String[] args) {
        SpringApplication.run(DeviceLocationApplication.class, args);
    }

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/iot/event/**")).build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(swaggerTitle, swaggerDescription, swaggerVersion, swaggerTermsOfServiceUrl,
                new Contact(swaggerContact, StringConstants.EMPTY_STRING, swaggerContactEmail), swaggerLicense,
                swaggerLicenseUrl, Collections.emptyList());
    }
}
