package com.josemiguelhyb.unitasks.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "UniTasks API", version = "v1", description = "API REST para gestionar usuarios y tareas en UniTasks.", contact = @Contact(name = "UniTasks", email = "support@unitasks.local"), license = @License(name = "Proprietary")), servers = {
        @Server(url = "http://localhost:8080", description = "Desarrollo local")
})
public class OpenAPIConfig {
}
