package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class PostFormacaoDTO {
	
	private UUID idinstrutor;
	private String formacao;
	private String conselho;
	private String registro;
	private String estado;	

}
