package br.com.jbst.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.entities.map.Empresa;
import lombok.Data;


@Data
public class RelatorioFaturamentoDTO {
	private UUID idfaturamento;
	private Instant dataHoraCriacao;
	private Integer numeroFaturamento;
    private String data_inicio;
    private String data_fim;
    private String venda;
    private String notafiscal;
    private String valor;
    private String comprador;
    private String telefone;
    private String email;
    private String responsavelfinanceiro;
    private String telefonefinanceiro;
    private String emailfinanceiro;
    private String whatsapp;
    private String observacoes;
    private List<RelatorioMatriculaDTO> matriculas;
	private BigDecimal total;
	private GetEmpresaDTOs empresa;

}
