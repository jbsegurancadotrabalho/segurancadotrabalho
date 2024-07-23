package br.com.jbst.DTO;


import java.time.Instant;
import java.util.UUID;


import lombok.Data;

@Data
public class PutTurmasDTO {
	
	private UUID idcurso;
	private UUID idUnidadedetreinamento;
	private UUID idTurmas;
	private Instant datainicio;
	private Instant datafim;
	private Instant validadedocurso;
	private String  cargahoraria;
	private String modalidade;
	private String status;
	private String instrutor;
	private String descricao;
	private String diasespecificos;
	private String tipo;
	private String nivel;
	private String validade;
	private String dia;
	private String mes;
	private String ano;
	private String observacoes;
	private String primeirodia;
	private String segundodia;
	private String terceirodia;
	private String quartodia;
	private String quintodia;
}
