package br.com.jbst.MatriculasDTO;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class PostMatriculaPedidosDTO {

	private UUID idTurmas;
	private UUID id;
	private UUID funcionario;
	private UUID idPedidos;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String tipo_de_pagamento;

}
