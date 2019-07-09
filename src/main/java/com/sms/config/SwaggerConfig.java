package com.sms.config;

import static springfox.documentation.builders.PathSelectors.any;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	  @Bean
	   public Docket api(){

		   return new Docket(DocumentationType.SWAGGER_2).groupName("user-service").select()
		                .apis(RequestHandlerSelectors.basePackage("com.sms"))
		                .paths(any())
		                .build()
		                .apiInfo(metaData());
		    }
	  
	  
	    private ApiInfo metaData() {
	        ApiInfo apiInfo = new ApiInfo(
	                "Spring Boot REST API",
	                "Spring Boot REST API for USER-SERVICE",
	                "1.0",
	                "Terms of service",
	                new Contact("Anchal Mathur", "https://springframework.guru/about/", "john@springfrmework.guru"),
	               "Apache License Version 2.0",
	                "https://www.apache.org/licenses/LICENSE-2.0");
	        return apiInfo;
	    }

}
