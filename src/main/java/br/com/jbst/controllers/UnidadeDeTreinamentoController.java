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

import br.com.jbst.DTO.GetUnidadeDeTreinamentoDTO;
import br.com.jbst.DTO.PostUnidadeDeTreinamentoDTO;
import br.com.jbst.DTO.PutUnidadeDeTreinamentoDTO;
import br.com.jbst.services.UnidadeDeTreinamentoService;



@RestController
@RequestMapping (value = "/api/unidadedetreinamento")
public class UnidadeDeTreinamentoController {

	
	@Autowired
	UnidadeDeTreinamentoService unidade;
	
	@PostMapping
	public ResponseEntity<GetUnidadeDeTreinamentoDTO> CriarUnidadeDeTreinamento(@RequestBody PostUnidadeDeTreinamentoDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(unidade.criarUnidadeDeTreinamento(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetUnidadeDeTreinamentoDTO> editarUnidadeDeTreinamento(@RequestBody PutUnidadeDeTreinamentoDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(unidade.editarUnidadeDeTreinamento(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetUnidadeDeTreinamentoDTO>> ConsultarUnidadesDeTreinamentos() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(unidade.consultarUnidadeDeTreinamentos(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetUnidadeDeTreinamentoDTO> consultarUmaUnidadeDeTreinamento(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(unidade.consultarUmaUnidadeDeTreinamento(id));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<GetUnidadeDeTreinamentoDTO> ExcluirUnidadeDeTreinamento (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(unidade.excluirUnidadeDeTreinamento(id));

	}
}
