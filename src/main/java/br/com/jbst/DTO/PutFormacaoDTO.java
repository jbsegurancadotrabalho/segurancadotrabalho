package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class PutFormacaoDTO {

	private UUID idinstrutor;
	private UUID idFormacao;
	private String formacao;
	private String conselho;
	private String registro;
	private String estado;
}
