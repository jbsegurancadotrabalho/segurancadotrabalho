package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;


@Data
public class GetFuncaoDTOs {

	
	private UUID idFuncao;
    private Instant dataHoraCriacao;
	private String funcao;
	private String cbo;
    private String descricao;
}
