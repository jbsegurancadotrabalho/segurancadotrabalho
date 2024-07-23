package br.com.jbst.DTO;

import java.math.BigDecimal;



import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMatriculaDTO {
	
	private UUID turmas;
	private UUID funcionario;
	private UUID faturamento;
	private UUID pessoafisica;
	private String venda;
	private BigDecimal valor;
	private String status;
    private String observacoes;
	
}
