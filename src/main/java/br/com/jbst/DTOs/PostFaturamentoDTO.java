package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PostFaturamentoDTO {
    private Instant data_inicio;
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
    private UUID idEmpresa;  
	public UUID getIdEmpresa() {
        return idEmpresa;
	}

}

