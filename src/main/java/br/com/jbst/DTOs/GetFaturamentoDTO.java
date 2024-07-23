package br.com.jbst.DTOs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.RelatorioMatriculaDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetFaturamentoDTO {
	private UUID idfaturamento;

    private Integer numeroFaturamento;

	@JsonSerialize(using = InstantSerializer.class)
    private Instant dataHoraCriacao;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant data_inicio;
	@JsonSerialize(using = InstantSerializer.class)
    private Instant	 data_fim;
    private List<RelatorioMatriculaDTO> matriculas;
   	private BigDecimal total;
    private String venda;
    private String notafiscal;
    private String valor;
    private String comprador;
    private String telefone;
    private String email;
    private String responsavelfinanceiro;
    private String telefonefinanceiro;
    private String emailfinanceiro;
    private String whatsapp;
    private String observacoes;
	private String parcelas;
	private String forma_de_pagamento;
	private GetEmpresaDTOs empresa;
    private String statusFatura;
	private boolean faturaFechada;
    public GetFaturamentoDTO toDTO() {
        GetFaturamentoDTO dto = new GetFaturamentoDTO();
        dto.setStatusFatura(this.faturaFechada ? "Fechada" : "Aberta");
        return dto;
    }

}
