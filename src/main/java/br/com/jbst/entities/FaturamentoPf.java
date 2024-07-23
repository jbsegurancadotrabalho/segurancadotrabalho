package br.com.jbst.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import br.com.jbst.entities.map.PessoaFisica;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "faturamentopf")
@NamedEntityGraph(name = "faturamentopf-with-pessoaFisica", attributeNodes = @NamedAttributeNode("pessoaFisica"))
public class FaturamentoPf {

	// Campo 1
	    @Id
	    @Column(name = "idfaturamentopf")
	    private UUID idfaturamentopf;

		// Campo 2
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "datahoracriacao", nullable = false)
	    private Instant dataHoraCriacao;

		// Campo 3
	    @Column(name = "numerofaturamento", nullable = true)
	    private Integer numeroFaturamento;

		// Campo 4
	    @Column(name = "data_inicio", nullable = false)
	    private Instant data_inicio;

		// Campo 5
	    @Column(name = "data_fim", nullable = false)
	    private Instant data_fim;

		// Campo 6
	    @Column(name = "venda", length = 50, nullable = false)
	    private String venda;
	    
		// Campo 7
	    @Column(name = "valor", length = 50, nullable = false)
	    private String valor;
	    
		// Campo 8
		@Column(name = "notafiscal", nullable = false)
		private String notafiscal;

		// Campo 9
	    @Column(name = "observacoes", length = 1000, nullable = false)
	    private String observacoes;

		// Campo 10
	    @Column(name = "fatura_fechada", nullable = false)
	    private boolean faturaFechada;
	    
		// Campo 11
	    @Column(name = "comprador", length = 50, nullable = true)
	    private String comprador;
		
	    // Campo 12
	    @Column(name = "telefone", length = 50, nullable = false)
	    private String telefone;
		
	    // Campo 13
	    @Column(name = "email", length = 50, nullable = false)
	    private String email;

		// Campo 14
	    @Column(name = "responsavelfinanceiro", length = 50, nullable = false)
	    private String responsavelfinanceiro;

		// Campo 15
	    @Column(name = "telefonefinanceiro", length = 50, nullable = false)
	    private String telefonefinanceiro;

		// Campo 16
	    @Column(name = "whatsapp", length = 50, nullable = false)
	    private String whatsapp;

		// Campo 17
	    @Column(name = "emailfinanceiro", length = 50, nullable = false)
	    private String emailfinanceiro;


		// Campo 18
	    @Column(name = "data_de_pagamento", length = 100, nullable = false)
	    private Instant data_de_pagamento;
		
		// Campo 19
	    @Column(name = "parcelas", length = 50, nullable = false)
	    private String parcelas;
		
		// Campo 20
	    @Column(name = "forma_de_pagamento", length = 50, nullable = false)
	    private String forma_de_pagamento;
	    
		// Campo 21
	    @Column(name = "total", nullable = true)
	    private BigDecimal total;

	    @OneToMany(mappedBy = "faturamentopf")
	    private List<Matriculas> matriculas;

	    public void calcularTotal() {
	        BigDecimal totalValue = BigDecimal.ZERO;

	        if (matriculas != null && !matriculas.isEmpty()) {
	            for (Matriculas matricula : matriculas) {
	                totalValue = totalValue.add(matricula.getValor());
	            }
	        }

	        this.total = totalValue;
	    }

	 
	    @OneToMany(mappedBy = "faturamentoPf")
	    private List<Cobranca> cobrancas;
	    
		// Campo 22
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "id_pessoa_fisica", nullable = false)
	    private PessoaFisica pessoaFisica;

   
}
