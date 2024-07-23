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

import br.com.jbst.DTOs.GetContatoDTOs;
import br.com.jbst.DTOs.PostContatoDTOs;
import br.com.jbst.DTOs.PutContatoDTOs;
import br.com.jbst.services.ContatoServices;



@RestController
@RequestMapping(value = "/api/contato")
public class ContatoController {
	
	
	@Autowired
	ContatoServices contatoService;
	
	
	@PostMapping
	public ResponseEntity<GetContatoDTOs> criarContato(@RequestBody PostContatoDTOs dto) throws Exception {	
		return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.criarContato(dto));		
	}
	@PutMapping
	public ResponseEntity<GetContatoDTOs> EditarContato(@RequestBody PutContatoDTOs dto) throws Exception {	
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(contatoService.editarContato(dto));

		
	}

	@GetMapping
	public ResponseEntity<List<GetContatoDTOs>> ConsultarContatos() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(contatoService.consultarContatos(toString()));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<GetContatoDTOs> ConsultarUmContato(@PathVariable("id") UUID id)throws Exception {
			return ResponseEntity.status(HttpStatus.OK).body(contatoService.consultarUmContato(id));
	}


	@DeleteMapping("{id}")
	public ResponseEntity<GetContatoDTOs> ExcluirContato (@PathVariable("id") UUID id) throws Exception {

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(contatoService.excluirContato(id));

	}


}

