package br.com.jbst.entities.map;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import br.com.jbst.entities.Matriculas;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario {
	
	// Campo 1
	@Id
	@Column(name = "idFuncionario")
	private UUID idFuncionario;
	
	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;
	
	// Campo 3
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	// Campo 4
	@Column(name = "cpf", length = 100, nullable = true)
	private String cpf;
	
	// Campo 5
	@Column(name = "rg", length = 100, nullable = false)
	private String rg;
	
	// Campo 6
	@Column(name = "status", length = 100, nullable = true)
	private String status;
	
	// Campo 7
	@Column(name = "matricula", length = 100, nullable = true)
	private String matricula;
	
	// Campo 8
	@Column(name = "assinatura", nullable = true)
	private byte[] assinatura;

	@OneToMany(mappedBy = "funcionario") 
	private List<Matriculas> matriculas;
	
	// Campo 9
	@ManyToOne // muitos funcionários para 1 empresa
	@JoinColumn(name = "empresa_id", nullable = false) // O JoinColumn é para mapeamento de chave estrangeira//
	private Empresa empresa;

	// Campo 10
	@ManyToOne // muitos funcionários para 1 função
	@JoinColumn(name = "funcao_id", nullable = false) 
	private Funcao funcao;
	

	
}
