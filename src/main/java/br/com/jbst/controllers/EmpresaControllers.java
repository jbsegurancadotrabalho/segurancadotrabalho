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

import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.services.EmpresaService;

@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaControllers {
	
	@Autowired
	EmpresaService  empresaService;
	
	
	@GetMapping
	public  ResponseEntity<List<GetEmpresaDTOs>> ConsultarEmpresas() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(empresaService.consultarEmpresas(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetEmpresaDTOs> ConsultarUmaEmpresa(@PathVariable("id") UUID id) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(empresaService.consultarUmaEmpresa(id));

	}
	

}
