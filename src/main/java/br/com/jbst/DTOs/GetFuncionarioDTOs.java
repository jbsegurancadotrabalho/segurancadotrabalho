package br.com.jbst.DTOs;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetFuncionarioDTOs {

	private UUID idFuncionario;
	private Instant DataHoraCriacao;
	private String nome;
	private String cpf;
	private String rg;
	private String status;
	private String funcao_certificado;
	private String whatsapp_funcionario;
	private String email_funcionario;
	private byte[] assinatura;
	private GetFuncaoDTOs funcao;
	private GetEmpresaDTOs empresa;

		
	}

	
		


