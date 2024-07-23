package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetContatoDTOs {
	
	private UUID idContato;
	private Instant dataHoraCriacao;
	private String contato;
	private String telefone_1;
	private String telefone_2;
	private String email;
}