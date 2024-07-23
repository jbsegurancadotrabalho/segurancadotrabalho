package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data	
public class PostContatoDTOs {
	
    private UUID idUnidadedetreinamento;
	private Instant dataHoraCriacao;
	private String contato;
	private String telefone_1;
	private String telefone_2;
	private String email;
}
