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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.jbst.DTO.GetInstrutorDTO;
import br.com.jbst.DTO.GetInstrutorDTOs;
import br.com.jbst.DTO.PostInstrutorDTO;
import br.com.jbst.DTO.PutInstrutorDTO;
import br.com.jbst.services.InstrutorService;

@RestController
@RequestMapping (value = "/api/instrutor")
public class InstrutorController {

	@Autowired
	InstrutorService instrutorService;

	@PostMapping
	public ResponseEntity<GetInstrutorDTO> criarInstrutor(@RequestBody PostInstrutorDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(instrutorService.criarInstrutor(dto));		

	}
	
	@PutMapping
	public ResponseEntity<GetInstrutorDTO> EditarInstrutor(@RequestBody PutInstrutorDTO dto) throws Exception {
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(instrutorService.editarInstrutor(dto));	
	}
	

	@GetMapping
	public  ResponseEntity<List<GetInstrutorDTO>> ConsultarInstrutores() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(instrutorService.consultarInstrutores(toString()));
	                                 }
	@GetMapping("{id}")
	public ResponseEntity<GetInstrutorDTO> consultarUmInstrutor(@PathVariable("id") UUID id) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(instrutorService.consultarInstrutorPorId(id));

	}
	
	@PutMapping(value = "incluir-assinatura", consumes = { "multipart/form-data" })
	public ResponseEntity<GetInstrutorDTOs> IncluirAssinatura(
			@RequestParam("id") UUID id,
			  @RequestParam("assinatura") MultipartFile assinatura
			) throws Exception {

		// capturando o tipo do arquivo
		String tipo = assinatura.getContentType();

		// verificando se o formato é válido (JPG, JPEG, PNG)
		if (tipo.equals("image/jpg") || tipo.equals("application/pdf") || tipo.equals("image/jpeg") || tipo.equals("image/png")) {
			// atualizando a foto do produto e retornando a resposta
			GetInstrutorDTOs result = instrutorService.incluirAssinatura(id, assinatura.getBytes());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<GetInstrutorDTOs> ExcluirInstrutor (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(instrutorService.excluirInstrutor(id));

	}
}
