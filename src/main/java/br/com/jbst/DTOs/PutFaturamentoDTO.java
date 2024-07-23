package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutFaturamentoDTO {
    private UUID idfaturamento;
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


}
