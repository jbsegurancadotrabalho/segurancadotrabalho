package br.com.jbst.CobrancasDTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutCobrancaDTO {

    private UUID idCobranca;
    private String responsavelCobranca;
    private String responsavelCliente;
    private Instant data_de_agendamento_pagamento;
    private String Assunto;
    private String Observacoes;

    // Outros métodos e construtores
}
