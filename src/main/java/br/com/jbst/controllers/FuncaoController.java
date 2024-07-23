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

import br.com.jbst.DTOs.GetFuncaoDTOs;
import br.com.jbst.services.FuncaoService;

@RestController
@RequestMapping(value = "/api/funcao")
public class FuncaoController {
	
	@Autowired
	FuncaoService  funcaoService;
	
	
	@GetMapping
	public  ResponseEntity<List<GetFuncaoDTOs>> ConsultarFuncoes() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(funcaoService.consultarFuncao(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetFuncaoDTOs> ConsultarUmaFuncao(@PathVariable("id") UUID id) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(funcaoService.consultarUmaFuncao(id));

	}
	

}
