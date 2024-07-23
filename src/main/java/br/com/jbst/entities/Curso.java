package br.com.jbst.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "curso")
public class Curso {
	
	@Id	// Campo 1
	@Column(name = "idcurso")
	private UUID idcurso;
	
	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;
	
	// Campo 3
	@Column(name = "curso", length = 500, nullable = false)
	private String curso;
	
	// Campo 4
	@Column(name = "codigo", nullable = false)
	 private Integer codigo;
	
	// Campo 5
	@Column(name = "descricao", length = 500, nullable = true)
	private String descricao;
	
	// Campo 6
	@Column(name = "conteudo", length = 5000, nullable = true)
	private String conteudo;
	
	// Campo 7
	@Column(name = "modelo_certificado", length = 100, nullable = true)
	private String modelo_certificado;
	
	// Campo 8
	@Column(name = "campo_especifico", length = 2000, nullable = true)
	private String campo_especifico;
	
	// Campo 9
	@Column(name = "tituloautorizacao", length = 200, nullable = true)
	private String tituloautorizacao;
	
	// Campo 10
	@Column(name = "itemdaautorizacao", length = 200, nullable = true)
	private String itemdaautorizacao;
	
	// Campo 11
	@Column(name = "conteudodaautorizacao", length = 5000, nullable = true)
	private String conteudodaautorizacao;
	
	// Campo 12
	@Column(name = "conformidade", length = 1000, nullable = true)
	private String conformidade;
	
	// Campo 13
	@Column(name = "valor_formacao", length = 1000, nullable = true)
	private String valorFormacao;
	
	// Campo 14
	@Column(name = "valor_reciclagem", length = 1000, nullable = true)
	private String valorReciclagem;
	
	// Campo 14
	@Column(name = "valorEad", length = 1000, nullable = true)
	private String valorEad;
	
	// Campo 15
	@Column(name = "composicao_orcamentaria", length = 5000, nullable = true)
	private String composicaoOrcamentaria;
	
	// Campo 16
	@Column(name = "observacoesGerais", length = 5000, nullable = true)
	private String observacoesGerais;
	
	// Campo 17
	@Column(name = "avaliacao", nullable = true)
	private byte[] avaliacao;
	
	// Campo 18
	@Column(name = "gabarito", nullable = true)
	private byte[] gabarito;
	
	// Campo 19
	@Column(name = "material", nullable = true)
	private byte[] material;
	
	@OneToMany(mappedBy = "curso") 
	private List <Turmas> turmas;

	

	
}
