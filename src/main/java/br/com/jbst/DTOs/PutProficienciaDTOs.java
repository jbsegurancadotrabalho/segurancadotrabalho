package br.com.jbst.DTOs;

import java.util.UUID;

import lombok.Data;

@Data
public class PutProficienciaDTOs {
	
	private UUID idinstrutor;
	private UUID idProficiencia;
	private String proficiencia;
	private String descricao;	
	private byte[] inserir_proficiencia;
}
