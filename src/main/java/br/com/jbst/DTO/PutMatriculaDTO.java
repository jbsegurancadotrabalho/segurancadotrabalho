package br.com.jbst.DTO;

import java.time.Instant;

import java.util.UUID;

import lombok.Data;

@Data
public class PutMatriculaDTO {
	private UUID idMatricula;
	private UUID idFuncionario;
	private UUID idPessoaFisica;
	private UUID idTurmas;
	private UUID idPedidos;
	private UUID idFaturamento;
    private Integer numeroMatricula;
	private Instant dataHoraCriacao;
	private String venda;
	private String valor;
	private String status;
    private String observacoes;
}
