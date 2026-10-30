package pe.edu.upeu.vetandes.config;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration
public class CorsConfig {
 @Bean public CorsConfigurationSource corsConfigurationSource(){
   CorsConfiguration conf=new CorsConfiguration();conf.setAllowedOrigins(List.of("http://localhost:4200"));
   conf.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
   conf.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Origin"));
   conf.setExposedHeaders(List.of("Location"));
   UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",conf);return source;
 }
}
