package br.com.jbst.CobrancasDTO;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetCobrancaFaturamentoDTO {
    private UUID idCobranca;
	@JsonSerialize(using = InstantSerializer.class)
    private Instant dataHoraCriacao;
    private String responsavelCobranca;
    private String responsavelCliente;
	private Instant data_de_agendamento_pagamento;
    private String Assunto;
    private String Observacoes;	 
}
