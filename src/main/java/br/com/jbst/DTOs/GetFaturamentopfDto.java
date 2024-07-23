package br.com.jbst.DTOs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.RelatorioMatriculaDTO;
import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import br.com.jbst.config.InstantSerializer;
import br.com.jbst.entities.map.PessoaFisica;
import lombok.Data;

@Data
public class GetFaturamentopfDto {
	private UUID idfaturamentopf;
    private Integer numeroFaturamento;
	@JsonSerialize(using = InstantSerializer.class)
    private Instant dataHoraCriacao;
	@JsonSerialize(using = InstantSerializer.class)
    private Instant data_inicio;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant data_fim;
    private String venda;
    private String notafiscal;
    private String valor;
    private String comprador;
    private String telefone;
    private String email;
    private String responsavelfinanceiro;
    private String telefonefinanceiro;
    private String whatsapp;
    private String emailfinanceiro;
    private Instant data_de_pagamento;
	private String parcelas;
	private String forma_de_pagamento;
    private String observacoes;
	private GetPessoaFisicaDTO pessoafisica;
	private List<RelatorioMatriculaDTO> matriculas;
	private BigDecimal total;
	private boolean faturaFechada;
}
