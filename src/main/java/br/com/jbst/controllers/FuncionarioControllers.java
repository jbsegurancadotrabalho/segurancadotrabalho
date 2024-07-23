package br.com.jbst.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DTO.GetFuncionarioDTO;
import br.com.jbst.DTOs.GetFuncionarioDTOs;
import br.com.jbst.services.FuncionarioService;




@RestController
@RequestMapping(value = "/api/funcionarios")
public class FuncionarioControllers {
	
	@Autowired
	FuncionarioService  funcionarioService;
	
	
	@GetMapping
	public  ResponseEntity<ResponseEntity<List<GetFuncionarioDTOs>>> ConsultarFuncionarios() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(funcionarioService.consultarFuncionarios());
	                                 }
	
	
	@RequestMapping("/api/funcionarios/todos")
	public  ResponseEntity<List<GetFuncionarioDTO>> ConsultarTodosFuncionarios() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(funcionarioService.consultarTodosFuncionarios(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetFuncionarioDTOs> ConsultarUmFuncionario(@PathVariable("id") UUID id) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.consultarUmFuncionario(id));

	}
	

}
