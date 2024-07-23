package br.com.jbst.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import lombok.Data;

@Data
public class RelatorioFaturamentoPfDto {
	private UUID idfaturamentopf;
	private Instant dataHoraCriacao;
    private String data_inicio;
    private String data_fim;
    private List<RelatorioMatriculaDTO> matriculas;
	private BigDecimal total;
	private GetPessoaFisicaDTO pessoafisica;

}
