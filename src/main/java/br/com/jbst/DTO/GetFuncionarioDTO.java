package br.com.jbst.DTO;

import java.util.UUID;

import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.entities.map.Empresa;
import lombok.Data;

@Data
public class GetFuncionarioDTO {

	private UUID idFuncionario;
	private String nome;
	private String cpf;
	private String rg;
	private GetEmpresaDTOs empresa;

}
