package br.com.jbst.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI().components(new Components()).info(new Info().title("API da Segurança do Trabalho - JBST Sistemas")
				.description("Nesta API temos Matriculas, Instrutor, Formação, Cursos, Turmas e Pedidos de Compras.").version("v1"));
	}
}