package br.com.jbst.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetPedidosDTO {

	
	private UUID idPedidos;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
	private String numerodopedido;
	private String venda;
	private String notafiscal;
	private String valor;
	private Integer creditos;
	private Integer matriculasrealizadas;
    private String comprador;
    private String telefone;
    private String email;
    private List<GetMatriculaDTO> matricula;
	private BigDecimal total;
	private GetEmpresaDTOs empresa;
	private boolean pedidoFechado;
	private Instant data_de_pagamento;
	private String responsavelfinanceiro;
	private String telefonefinanceiro;
	private String whatsapp;
	private String emailfinanceiro;
	private String parcelas;
	private String forma_de_pagamento;
    private String observacoes;

}
