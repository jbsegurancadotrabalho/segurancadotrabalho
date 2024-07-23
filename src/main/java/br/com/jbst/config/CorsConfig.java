package br.com.jbst.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permitir CORS para todos os endpoints
                .allowedOriginPatterns("*") // Permitir solicitações de qualquer origem
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permitir estes métodos HTTP
                .allowedHeaders("*") // Permitir todos os cabeçalhos
                .allowCredentials(true) // Permitir o envio de credenciais como cookies
                .exposedHeaders("Authorization"); // Expor cabeçalhos adicionais, se necessário
    }
}

