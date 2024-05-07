package Backend.MASJIB.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info  = new Info()
                .version("v1")
                .title("극비 프로젝트")
                .description("MASJIB 기술 명세서");

        String tokenSchema = "JWT 토큰값";
        // API 요청 Header에 JWT 토큰값을 넣어야 함을 명시
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(tokenSchema);
        // Security Schemes 등록
        Components components = new Components()
                .addSecuritySchemes(tokenSchema, bearerAuthSecurityScheme(tokenSchema));


        // Server
        Server server = new Server();
        server.setUrl("/");
        server.setDescription("기본 서버");

        Server server2 = new Server();
        server2.setUrl("https://api.sangwon.xyz");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server, server2))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private SecurityScheme bearerAuthSecurityScheme(String tokenSchema) {
        return new SecurityScheme()
                .name(tokenSchema)
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }
}
