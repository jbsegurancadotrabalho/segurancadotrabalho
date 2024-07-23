package br.com.jbst.entities;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "cobranca")
public class Cobranca {

	// Campo 1
    @Id
    @Column(name = "idCobranca")
    private UUID idCobranca;

	// Campo 2
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datahoracriacao", length = 100, nullable = false)
    private Instant dataHoraCriacao;


	// Campo 3
    @Column(name = "responsavelCobranca", length = 100, nullable = false)
    private String responsavelCobranca;


	// Campo 4
    @Column(name = "responsavelCliente", length = 100, nullable = false)
    private String responsavelCliente;
 

	// Campo 5
    @Column(name = "data_de_agendamento_pagamento", length = 100, nullable = false)
    private Instant data_de_agendamento_pagamento;
	

	// Campo 6
    @Column(name = "Assunto", length = 100, nullable = false)
    private String Assunto;
	
	// Campo 7
	@Column(name = "Observacoes", length = 1000, nullable = false)
    private String Observacoes;

	// Campo 8
    @ManyToOne
    @JoinColumn(name = "idfaturamento" )
    private Faturamento faturamento;


	// Campo 9
	@ManyToOne
	@JoinColumn(name = "idfaturamentopf")
	private FaturamentoPf faturamentoPf;

	// Campo 10
	@ManyToOne
    @JoinColumn(name = "idpedidos")
    private Pedidos pedidos;

  
}
