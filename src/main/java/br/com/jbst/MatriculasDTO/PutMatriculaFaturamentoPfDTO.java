package br.com.jbst.MatriculasDTO;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class PutMatriculaFaturamentoPfDTO {
	
	
	private UUID idMatricula;
	private UUID idTurmas;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String tipo_de_pagamento;
    private String observacoes;
	public Object getIdFuncionario() {
		// TODO Auto-generated method stub
		return null;
	}

}
