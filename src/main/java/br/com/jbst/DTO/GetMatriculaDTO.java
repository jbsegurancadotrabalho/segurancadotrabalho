package br.com.jbst.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTOs.GetEvidenciasDTOs;
import br.com.jbst.DTOs.GetFuncionarioDTOs;
import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetMatriculaDTO {
	private UUID idMatricula;
    private Integer numeroMatricula;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String tipo_de_pagamento;
	private String observacoes;
	private BigDecimal total;
	private List <GetEvidenciasDTOs> evidencias;
	private GetPessoaFisicaDTO pessoafisica;
	private GetFuncionarioDTOs funcionario;
	private GetTurmasDTO turmas;

}
