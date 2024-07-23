package br.com.jbst.entities;
import java.time.Instant;

import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.map.Empresa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "turmas")
public class Turmas {
	
	
	@Id 	// Campo 1
	@Column(name = "idturmas")
	private UUID idTurmas;
	
	// Campo 2
	@Column(name = "numeroturma", nullable = true)
	private Integer numeroTurma;
	
	// Campo 3
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datainicio", nullable = true)
	private Instant datainicio;
	
	// Campo 4
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datafim", nullable = true)
	private Instant datafim;
	
	// Campo 5
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validadedocurso", nullable = true)
	private Instant validadedocurso;
	
	// Campo 6
	@Column(name = "cargahoraria", nullable = true)
	private String  cargahoraria;
	
	// Campo 7
	@Column(name = "modalidade", nullable = true)
	private String modalidade;
	
	// Campo 8
	@Column(name = "status", nullable = true)
	private String status;
	
	// Campo 9
	@Column(name = "instrutor", nullable = true)
	private String instrutor;
	
	// Campo 10
	@Column(name = "descricao", nullable = true)
	private String descricao;
	
	// Campo 11
	@Column(name = "diasespecificos", nullable = true)
	private String diasespecificos;
	
	// Campo 12
	@Column(name = "tipo", nullable = true)
	private String tipo;
	
	// Campo 13
	@Column(name = "nivel", nullable = true)
	private String nivel;
	
	// Campo 14
	@Column(name = "validade", nullable = true)
	private String validade;
	
	// Campo 15
	@Column(name = "dia", nullable = true)
	private String dia;
	
	// Campo 16
	@Column(name = "mes", nullable = true)
	private String mes;
	
	// Campo 17
	@Column(name = "ano", nullable = true)
	private String ano;
	
	// Campo 18
	@Column(name = "primeirodia", nullable = true)
	private String primeirodia;

	// Campo 19
	@Column(name = "segundodia", nullable = true)
	private String segundodia;

	// Campo 20
	@Column(name = "terceirodia", nullable = true)
	private String terceirodia;

	// Campo 21
	@Column(name = "quartodia", nullable = true)
	private String quartodia;

	// Campo 22
	@Column(name = "quintodia", nullable = true)
	private String quintodia;
	
	// Campo 23
	@Column(name = "observacoes_gerais ", nullable = true)
	private String observacoes;
    
	// Campo 24
	@Column(name = "turma_fechada", nullable = false)
	private boolean turmaFechada;
	
	// Campo 25
	@Column(name = "matriculas_bloqueadas", nullable = false)
	private boolean matriculasBloqueadas;
	
	// Campo 26
    @ManyToOne
    @JoinColumn(name = "idunidadedetreinamento", referencedColumnName = "idUnidadedetreinamento", nullable = true)
    private UnidadeDeTreinamento unidadeDeTreinamento;
	
	
	// Campo 27
	@ManyToOne // muitos contatos  para 1 empresa
	@JoinColumn(name = "idcurso", nullable = true) // O JoinColumn é para mapeamento de chave estrangeira//
	private Curso curso;
    
	@OneToMany(mappedBy = "turmas") //1 Empresa tem muitos Funcionários
	private List<Matriculas> matricula;
	

	  @ManyToMany
	    @JoinTable(
	        name = "turma_instrutor",
	        joinColumns = @JoinColumn(name = "idturmas"),
	        inverseJoinColumns = @JoinColumn(name = "idinstrutor", nullable = true )
	    )
	    private List<Instrutor> instrutores;


	public Empresa getEmpresa() {
		return null;
	}
	
	
	public void turmaAberta() {
        if (this.turmaFechada) {
            this.turmaFechada = false;
            this.matriculasBloqueadas = false;
        }
    }

    public void turmaFechada() {
        if (!this.turmaFechada) {
            this.turmaFechada = true;
            this.matriculasBloqueadas = true;
        }
    }
	
}
