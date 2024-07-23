package br.com.jbst.entities.map;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.Pedidos;
import br.com.jbst.entities.Usuario;
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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "empresa")
public class Empresa {

// Campo 1	
@Id
@Column(name = "idempresa")
private UUID idEmpresa;

//Campo 2
@Temporal(TemporalType.TIMESTAMP)
@Column(name = "datahoracriacao", nullable = false) 
private Instant dataHoraCriacao;

//Campo 3
@Column(name = "razaosocial", length = 100, nullable = false)
private String razaosocial;

//Campo 4
@Column(name = "nomefantasia", length = 20, nullable = false)
private String nomefantasia;

//Campo 5
@Column(name = "cnpj", length = 100, nullable = false)
private String cnpj;

//Campo 6
@Column(name = "status", length = 100, nullable = false)
private String status;

//Campo 7
@Column(name = "inscricaoestadual", length = 100, nullable = true)
private String inscricaoestadual;

//Campo 8
@Column(name = "inscricaomunicipal", length = 100, nullable = true)
private String inscricaomunicipal;

//Campo 9	
@Column(name = "assinatura")
private byte[] logo;

//Campo 10
@Column(name = "responsavel_sistema", length = 100, nullable = true)
private String responsavel_sistema;

//Campo 11
@Column(name = "email_usuario", length = 100, nullable = true)
private String email_usuario;

//Campo 12
@Column(name = "senha_sistema", length = 100, nullable = true)
private String senha_sistema;

//Campo 13
@Column(name = "telefone_responsavel", length = 100, nullable = true)
private String telefone_responsavel;

@OneToMany(mappedBy = "empresa") //1 Empresa tem muitos Funcionários
private List<Funcionario> funcionarios;

@OneToMany(mappedBy = "empresa")
private List<Faturamento> faturamentos;

@OneToMany(mappedBy = "empresa") // 1 Empresa tem muitos Pedidos
private List<Pedidos> pedidos;

public Object getId() {
	// TODO Auto-generated method stub
	return null;
}

@ManyToOne // muitos funcionários para 1 empresa
@JoinColumn(name = "idusuario", nullable = true)  
private Usuario usuario;


}
