package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutEvidenciasDTOs {

	private UUID idMatricula;
	private UUID idEvidencias;
	private String nome;
	private String descricao;	
	private byte[] inserir_evidencias;
}
