package br.com.jbst.MatriculasDTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetPessoaFisicaFaturamentoDto {
	private UUID idpessoafisica;
    private Instant dataHoraCriacao;
	private String pessoafisica;
	private String rg;
    private String cpf;
	private String telefone_1;
	private String telefone_2;
	private String email;
}
