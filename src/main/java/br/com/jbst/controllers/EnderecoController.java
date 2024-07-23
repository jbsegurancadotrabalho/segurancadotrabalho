package br.com.jbst.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.jbst.DTOs.GetEnderecoDTO;
import br.com.jbst.DTOs.PostEnderecoDTO;
import br.com.jbst.DTOs.PutEnderecoDTO;
import br.com.jbst.services.EnderecoService;


@RestController
@RequestMapping(value = "/api/endereco")
public class EnderecoController {
	
	
	@Autowired
    EnderecoService  enderecoService;
	
	
	@PostMapping
	public ResponseEntity<GetEnderecoDTO> CriarEndereco(@RequestBody PostEnderecoDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criarEndereco(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetEnderecoDTO> editarEndereco(@RequestBody PutEnderecoDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(enderecoService.editarEndereco(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetEnderecoDTO>> ConsultarEndereco() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(enderecoService.consultarEndereco(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetEnderecoDTO> consultarUmEndereco(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(enderecoService.consultarUmEndereco(id));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<GetEnderecoDTO> ExcluirEndereco (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(enderecoService.excluirEndereco(id));

	}
}


