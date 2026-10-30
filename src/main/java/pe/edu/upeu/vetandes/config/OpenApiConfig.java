package pe.edu.upeu.vetandes.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;
@Configuration
public class OpenApiConfig {
 @Bean OpenAPI openApi(){return new OpenAPI().info(new Info().title("VetAndes API").version("v1"))
 .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
 .components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));}
}
