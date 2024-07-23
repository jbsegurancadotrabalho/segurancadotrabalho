package br.com.jbst.MatriculasDTO;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTOs.GetFuncionarioDTOs;
import lombok.Data;

@Data
public class GetMatriculaFaturamentoPjDTO {
	private UUID idMatricula;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String observacoes;
	private String tipo_de_pagamento;
	private GetPessoaFisicaDTO pessoafisica;
	private GetTurmasDTO turmas;
}
