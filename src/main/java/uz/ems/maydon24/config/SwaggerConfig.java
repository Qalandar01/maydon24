package uz.ems.maydon24.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
@SecurityRequirement(name = "bearerAuth")
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${app.swagger.server-url:http://localhost:8080}") String serverUrl) {
        return new OpenAPI()
                .info(new Info()
                        .title("Maydon24")
                        .version("2.0")
                        .description("API Documentation"))
                .addServersItem(new Server().url(serverUrl).description("API Server"));
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            Parameter languageParameter = new Parameter()
                    .in("header")
                    .name("Accept-Language")
                    .description("Language selection (en, uz, ru)")
                    .example("en")
                    .schema(new io.swagger.v3.oas.models.media.Schema().type("string")._enum(java.util.Arrays.asList("en", "uz", "ru")));

            operation.addParametersItem(languageParameter);
            return operation;
        };
    }
}
