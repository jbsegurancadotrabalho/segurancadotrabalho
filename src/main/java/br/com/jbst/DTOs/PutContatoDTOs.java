package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutContatoDTOs {
	
    private UUID idUnidadedetreinamento;
	private UUID idContato;
	private Instant dataHoraCriacao;
	private String contato;
	private String telefone_1;
	private String telefone_2;
	private String email;
}
