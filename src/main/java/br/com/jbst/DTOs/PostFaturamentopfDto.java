package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PostFaturamentopfDto {
	private UUID idpessoafisica;
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
    public UUID getIdpessoafisica() {
        return idpessoafisica;
    }

    public void setIdpessoafisica(UUID idpessoafisica) {
        this.idpessoafisica = idpessoafisica;
    }
}
