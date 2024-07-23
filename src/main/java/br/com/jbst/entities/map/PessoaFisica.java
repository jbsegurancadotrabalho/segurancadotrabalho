package br.com.jbst.entities.map;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.FaturamentoPf;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoafisica")
public class PessoaFisica {

	@Id // Campo 1
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idpessoafisica", updatable = false, nullable = false)
	private UUID idpessoafisica;

	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;

	// Campo 3
	@Column(name = "pessoafisica", length = 100, nullable = false)
	private String pessoafisica;

	// Campo 4
	@Column(name = "rg", length = 100)
	private String rg;

	// Campo 5
	@Column(name = "cpf", length = 100)
	private String cpf;

	// Campo 6
	@Column(name = "telefone_1", length = 100, nullable = false)
	private String telefone1;

	// Campo 7
	@Column(name = "telefone_2", length = 100)
	private String telefone2;

	// Campo 8
	@Column(name = "email", length = 100)
	private String email;

	// Campo 9
	@Column(name = "senha_sistema", length = 100, nullable = true)
	private String senha_sistema;
	
	// Campo 10
	@Column(name = "assinatura_pessoafisica")
	private byte[] assinaturaPessoaFisica;

	@OneToMany(mappedBy = "pessoafisica")
	private List<Matriculas> matriculas;

	@OneToMany(mappedBy = "pessoaFisica")
	private List<FaturamentoPf> faturamentos;

	public UUID getIdpessoafisica() {
		return idpessoafisica;
	}
	
	// Campo 11
	@ManyToOne // muitos funcionários para 1 empresa
	@JoinColumn(name = "id_usuario_da_pessoa_fisica", nullable = true)
	private Usuario usuario;
}
