package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetProficienciaDTOs {
	
	private UUID idProficiencia;
	private Instant dataHoraCriacao;
	private String proficiencia;
	private String descricao;	
	private byte[] inserir_proficiencia;
}
