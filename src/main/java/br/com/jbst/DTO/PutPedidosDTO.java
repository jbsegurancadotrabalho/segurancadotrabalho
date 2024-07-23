package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutPedidosDTO {
	
	private UUID idPedidos;
	private Instant data_de_pagamento;
	private String venda;
	private String notafiscal;
	private String valor;
	private Integer creditos;
	private String comprador;
	private String telefone;
	private String email;
	private String responsavelfinanceiro;
	private String telefonefinanceiro;
	private String whatsapp;
	private String emailfinanceiro;
	private String parcelas;
	private String forma_de_pagamento;
    private String observacoes;
}
