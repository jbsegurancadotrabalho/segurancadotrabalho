package br.com.jbst.DTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.jbst.DTOs.GetProficienciaDTOs;
import lombok.Data;

@Data
public class GetInstrutorDTO {
	
	private UUID idinstrutor;
	private Instant dataHoraCriacao;
	private String instrutor;
	private String rg;
	private String cpf;
	private String telefone_1;
	private String telefone_2;
	private String email;
	private byte[] assinatura_instrutor;
	private List<GetProficienciaDTOs> proficiencias;
	private List<GetFormacaoDTO> formacoes;
	
}
