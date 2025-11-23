package uz.ems.maydon24.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Maydon24", // Optional: Qo'shimcha ma'lumot berish uchun
                version = "2.0", // OpenAPI versiyasi noto'g'ri emas, faqat ma'lumot sifatida
                description = "API Documentation"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
        },
        security = @SecurityRequirement(name = "bearerAuth")
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
public class SwaggerConfig {
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
