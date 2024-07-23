package br.com.jbst.entities.map;

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
@Table(name = "funcao")
public class Funcao {

	// Campo 1
	@Id
	@Column(name = "idFuncao")
	private UUID idFuncao;
	
	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;
	
	// Campo 3
	@Column(name = "funcao", length = 100, nullable = false)
	private String funcao;
	
	// Campo 4
	@Column(name = "cbo", length = 100, nullable = false)
	private String cbo;
	
	// Campo 5
	@Column(name = "descricao", length = 1000, nullable = false)
	private String descricao;
	
	// Campo 6
	@Column(name = "gro", length = 100, nullable = true)
	private String gro;
	
	// Campo 7
	@Column(name = "gp", length = 100, nullable = true)
	private String gp;
	
	@OneToMany(mappedBy = "funcao") //1 função tem muitos funcionários
	private List<Funcionario> funcionarios;
}
