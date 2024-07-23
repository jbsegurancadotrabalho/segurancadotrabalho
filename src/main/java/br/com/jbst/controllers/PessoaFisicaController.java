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

import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import br.com.jbst.services.PessoaFisicaService;
@RestController
@RequestMapping(value = "/api/pessoa-fisica")
public class PessoaFisicaController {
	@Autowired
	PessoaFisicaService  pessoaFisicaService;
	
	
	
	@GetMapping
	public  ResponseEntity<List<GetPessoaFisicaDTO>> ConsultarPessoaFisica() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(pessoaFisicaService.consultarPessoaFisica());
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity< GetPessoaFisicaDTO> consultarUmaPessoaFisica(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(pessoaFisicaService.consultarUmaPessoaFisica(id));

	}
	
}
