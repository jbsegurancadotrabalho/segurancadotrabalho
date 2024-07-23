package br.com.jbst.DTOs;

import java.util.UUID;

import lombok.Data;

@Data
public class PostProficienciaDTOs {

	private UUID idinstrutor;
	private String proficiencia;
	private String descricao;	

}
