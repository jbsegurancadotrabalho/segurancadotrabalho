package br.com.jbst;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRabbit
@SpringBootApplication
public class SegurancadotrabalhoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SegurancadotrabalhoApplication.class, args);
	}

}
