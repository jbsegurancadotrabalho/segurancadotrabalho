package br.com.jbst.DTO;

import java.time.Instant;

import java.util.UUID;
import lombok.Data;

@Data
public class GetCursoDTO {
    private UUID idcurso;
	private Instant dataHoraCriacao;
	private Integer codigo;
	private String curso;
	private String status;
	private String descricao;
	private String conteudo;
	private String modelo_certificado;
	private String campo_especifico;
	private String tituloautorizacao;
	private String itemdaautorizacao;
	private String conteudodaautorizacao;
	private String conformidade;
	private String valorFormacao;
	private String valorReciclagem;
	private String composicaoOrcamentaria;
	private String observacoesGerais;
	private String valorEad;

}
