package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class PostInstrutorDTO {
	
	private UUID idFormacao;
	private String instrutor;
	private String rg;
	private String cpf;
	private String telefone_1;
	private String telefone_2;
	private String email;

}
