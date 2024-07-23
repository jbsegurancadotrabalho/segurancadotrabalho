package br.com.jbst.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.map.Empresa;
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
@Table(name = "faturamento")
public class Faturamento {

	// Campo 1
    @Id
    @Column(name = "idfaturamento")
    private UUID idfaturamento;

	// Campo 2
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datahoracriacao", nullable = false)
    private Instant dataHoraCriacao;

	// Campo 3
    @Column(name = "numerofaturamento", nullable = true)
    private Integer numeroFaturamento;

	// Campo 4
    @Column(name = "data_inicio", length = 100, nullable = false)
    private Instant data_inicio;

	// Campo 5
    @Column(name = "data_fim", length = 100, nullable = false)
    private Instant data_fim;

	// Campo 6
    @Column(name = "venda", length = 50, nullable = false)
    private String venda;

	// Campo 7
    @Column(name = "notafiscal", length = 50, nullable = true)
    private String notafiscal;

	// Campo 8
    @Column(name = "valor", length = 50, nullable = false)
    private String valor;

	// Campo 9
    @Column(name = "comprador", length = 50, nullable = false)
    private String comprador;

	// Campo 10
    @Column(name = "telefone", length = 50, nullable = false)
    private String telefone;

	// Campo 11
    @Column(name = "email", length = 50, nullable = false)
    private String email;

	// Campo 12
    @Column(name = "responsavelfinanceiro", length = 50, nullable = false)
    private String responsavelfinanceiro;

	// Campo 13
    @Column(name = "telefonefinanceiro", length = 50, nullable = false)
    private String telefonefinanceiro;

	// Campo 14
    @Column(name = "whatsapp", length = 50, nullable = false)
    private String whatsapp;

	// Campo 15
    @Column(name = "emailfinanceiro", length = 50, nullable = false)
    private String emailfinanceiro;

	// Campo 16
    @Column(name = "data_de_pagamento", length = 100, nullable = false)
    private Instant data_de_pagamento;
	
	// Campo 17
    @Column(name = "parcelas", length = 50, nullable = false)
    private String parcelas;
	
	// Campo 18
    @Column(name = "forma_de_pagamento", length = 50, nullable = false)
    private String forma_de_pagamento;

	// Campo 19
    @Column(name = "observacoes", length = 1000, nullable = false)
    private String observacoes;

	// Campo 20
    @Column(name = "total", length = 100, nullable = true)
    private BigDecimal total;

    @OneToMany(mappedBy = "faturamento")
    private List<Matriculas> matriculas;
	
    // Campo 21
    @Column(name = "fatura_fechada", nullable = false)
    private boolean faturaFechada;
    
	// Campo 22
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    // Adicionando a lista de cobranças associadas a um faturamento
    @OneToMany(mappedBy = "faturamento")
    private List<Cobranca> cobrancas;

    public void calcularTotal() {
        BigDecimal totalValue = BigDecimal.ZERO;

        if (matriculas != null && !matriculas.isEmpty()) {
            for (Matriculas matricula : matriculas) {
                totalValue = totalValue.add(matricula.getValor());
            }
        }

        this.total = totalValue;
    }

    public boolean isFaturaAberta() {
        Instant dataAtual = Instant.now();
        return dataAtual.isAfter(this.data_inicio) && dataAtual.isBefore(this.data_fim);
    }

    public void fecharMatriculasAposDataFim() {
        Instant dataAtual = Instant.now();
        if (dataAtual.isAfter(this.data_fim) && !this.faturaFechada && this.matriculas != null && !this.matriculas.isEmpty()) {
            for (Matriculas matricula : this.matriculas) {
                matricula.setFaturaFechada(true);
            }
            this.faturaFechada = true;
        }
    }

  
}
