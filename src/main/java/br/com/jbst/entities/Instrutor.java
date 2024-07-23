 package br.com.jbst.entities;
import java.time.Instant;

import java.util.List;
import java.util.UUID;
import br.com.jbst.entities.map.Formacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "instrutor")
public class Instrutor {

	// Campo 1	
	@Id
	@Column(name = "idinstrutor")
	private UUID idinstrutor;
	
	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;
	
	// Campo 3
	@Column(name = "instrutor", length = 100, nullable = false)
	private String instrutor;
	
	// Campo 4
	@Column(name = "rg", length = 100, nullable = true)
	private String rg;
	
	// Campo 5
	@Column(name = "cpf", length = 100, nullable = true)
	private String cpf;
	
	// Campo 6
	@Column(name = "telefone_1", length = 100, nullable = false)
	private String telefone_1;
	
	// Campo 7
	@Column(name = "telefone_2", length = 100, nullable = true)
	private String telefone_2;
	
	// Campo 8
	@Column(name = "email", length = 100, nullable = true)
	private String email;

	// Campo 9
	@Column(name = "assinatura_instrutor", nullable = true)
	private byte[] assinatura_instrutor;
	

	// Campo 10
	@Column(name = "proficiencia", nullable = true)
	private byte[] proficiencia;
	
	@OneToMany(mappedBy = "instrutores") //1 pessoa fisica tem uma lista de formaçoes
	private List<Formacao> formacoes;
	

	 
	 @ManyToMany(mappedBy = "instrutores")
	 private List<Turmas> turmas;

	 
	 @OneToMany(mappedBy = "instrutores") //1 pessoa fisica tem uma lista de formaçoes
	private List<Proficiencia> proficiencias;
	
}
