package com.chess.jnd.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Jenny",
                        email = "Bulish@yandex.ru",
                        url = "https://jand.com"
                ),
                description = "Open Api documentation for chess project",
                title = "Open api chess project",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production ENV",
                        url = "http://jand.com"
                )
        }
)
public class OpenApiConfig { }
