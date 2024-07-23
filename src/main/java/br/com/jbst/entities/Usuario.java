package br.com.jbst.entities;
import java.util.ArrayList;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.map.Empresa;
import br.com.jbst.entities.map.PessoaFisica;
import jakarta.persistence.CascadeType;
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

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

	// Campo 1
	@Id
	@Column(name = "id")
	private UUID id;

	// Campo 2
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	// Campo 3
	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;

	// Campo 4
	@Column(name = "senha", length = 40, nullable = false)
	private String senha;

	// Campo 5
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;

	// Campo 6
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoraalteracao", nullable = false)
	private Instant dataHoraAlteracao;

	// Campo 7
	@ManyToOne // muitos usuários para 1 perfil
	@JoinColumn(name = "perfil_id", nullable = false) // O JoinColumn é para mapeamento de chave estrangeira//
	private Perfil perfil;
	

	@ManyToMany(mappedBy = "usuarios")
    private List<Matriculas> matriculas;

	@OneToMany(mappedBy = "usuario") //1 Empresa tem muitos Funcionários
	private List<Empresa> empresas;


	@OneToMany(mappedBy = "usuario") //1 Empresa tem muitos Funcionários
	private List<PessoaFisica> pessoasfisica;
}
