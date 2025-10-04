package com.rishipadala.journalApp.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.springdoc.core.utils.SpringDocUtils.getConfig;

@Configuration
public class SwaggerConfig {
    //STATIC BLOCK to convert ObjectId to String
    static {
        getConfig().replaceWithClass(ObjectId.class, String.class);
    }
    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                        new Info().title("MyJournalApp APIs")
                                .version("v1.0")
                                .description("A secure and personal journaling application REST API. This documentation provides a detailed overview of all available endpoints. \n Made By Rishi Padala")
                                .contact(new Contact()
                                        .name("Rishi Padala")
                                        .email("rvpadala20@gmail.com") // Replace with your email
                                        .url("https://github.com/rishipadala/JournalApp")) // Replace with your project URL

                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://myjournalapp-0c5f.onrender.com").description("Live Production Server")
                ))

/*                .tags(List.of(
                        new Tag().name("Public APIs"),
                        new Tag().name("USER APIs"),
                        new Tag().name("Journal APIs"),
                        new Tag().name("Admin APIs")
               ))

        --> THE PREFERENCE SETTING OF THE APIs Are no Longer Supported by swagger
               - According to Latest Documentation of Swagger OpenAPI
 */
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }

}
