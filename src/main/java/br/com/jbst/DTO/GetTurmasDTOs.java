package br.com.jbst.DTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.config.InstantSerializer;
import lombok.Data;


@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idTurmas")
public class GetTurmasDTOs {

	private UUID idTurmas;
	
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
    
	private Integer numeroTurma;
	
	@JsonSerialize(using = InstantSerializer.class)
	private Instant datainicio;
    
	@JsonSerialize(using = InstantSerializer.class)
	private Instant datafim;
	
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
	private String primeirodia;
	private String segundodia;
	private String terceirodia;
	private String quartodia;
	private String quintodia;
	private String observacoes;
    private List<GetAssinaturaInstrutorDTO> assinaturasInstrutores;
	private GetCursoDTO curso;
	private GetUnidadeDeTreinamentoDTO unidadeDeTreinamento;
	private List<GetInstrutorDTO> instrutores;
	private List<GetMatriculaTurmDTO> matricula;



}
