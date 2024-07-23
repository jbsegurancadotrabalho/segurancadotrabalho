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

import br.com.jbst.DTO.GetFormacaoDTO;
import br.com.jbst.DTO.PostFormacaoDTO;
import br.com.jbst.DTO.PutFormacaoDTO;
import br.com.jbst.services.FormacaoService;

@RestController
@RequestMapping(value = "/api/formacao")
public class FormacaoController {

	@Autowired
	FormacaoService formacaoService;

	@PostMapping
	public ResponseEntity<GetFormacaoDTO> criarFormacao(@RequestBody PostFormacaoDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(formacaoService.criarFormacao(dto));		

	}

	@PutMapping
	public ResponseEntity<GetFormacaoDTO> editarFormacao(@RequestBody PutFormacaoDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(formacaoService.editarFormacao(dto));

	}

	@GetMapping
	public ResponseEntity<List<GetFormacaoDTO>> ConsultarFormacao() {
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(formacaoService.consultarFormacao());	
	}

	@GetMapping("{id}")
	public  ResponseEntity<GetFormacaoDTO> consultarUmaFormacao(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(formacaoService.consultarUmaFormacaoService(id));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<GetFormacaoDTO> ExcluirFormacao (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(formacaoService.excluirFormacao(id));

	}
}