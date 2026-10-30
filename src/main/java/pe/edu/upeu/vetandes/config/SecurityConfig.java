package pe.edu.upeu.vetandes.config;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import pe.edu.upeu.vetandes.security.*;
@Configuration @EnableWebSecurity @EnableMethodSecurity
public class SecurityConfig {
 @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean public SecurityFilterChain security(HttpSecurity http,JwtService jwt,UserDetailsService uds,@Qualifier("corsConfigurationSource") CorsConfigurationSource cors,
         RestAuthenticationEntryPoint entry,RestAccessDeniedHandler denied)throws Exception {
   return http.csrf(AbstractHttpConfigurer::disable).logout(AbstractHttpConfigurer::disable)
    .cors(c->c.configurationSource(cors))
    .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .exceptionHandling(e->e.authenticationEntryPoint(entry).accessDeniedHandler(denied))
    .authorizeHttpRequests(a->a
       .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
       .requestMatchers("/api/v1/auth/login","/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**","/error").permitAll()
       .anyRequest().authenticated())
    .addFilterBefore(new JwtAuthenticationFilter(jwt,uds),UsernamePasswordAuthenticationFilter.class)
    .build();
 }
}
