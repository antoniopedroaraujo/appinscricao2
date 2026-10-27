package com.aula.pos.appinscricao;

import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.service.impl.CepServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AppinscricaoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppinscricaoApplication.class, args);
	}
}

