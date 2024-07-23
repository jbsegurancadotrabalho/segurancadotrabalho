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

import br.com.jbst.DTO.GetCursoDTO;
import br.com.jbst.DTO.PostCursoDTO;
import br.com.jbst.DTO.PutCursoDTO;
import br.com.jbst.services.CursoService;

@RestController
@RequestMapping(value = "/api/cursos")
public class CursoController {

	@Autowired
	CursoService  cursoService;
	
	@PostMapping
	public ResponseEntity<GetCursoDTO> CriarCurso(@RequestBody PostCursoDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.criarCurso(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetCursoDTO> editarCurso(@RequestBody PutCursoDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(cursoService.editarCurso(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetCursoDTO>> ConsultarCursos() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(cursoService.consultarCursos(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetCursoDTO> consultarUmInstrutor(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(cursoService.consultarUmCurso(id));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<GetCursoDTO> ExcluirCurso (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(cursoService.excluirCurso(id));

	}
	
	@PutMapping(value = "incluir-avaliacao", consumes = { "multipart/form-data" })
	public ResponseEntity<GetCursoDTO> IncluirAvaliacao(@RequestParam("id") UUID id,
			@RequestParam("avaliacao") MultipartFile avaliacao) throws Exception {

		// capturando o tipo do arquivo
		String tipo = avaliacao.getContentType();

		// verificando se o formato é válido (JPG, JPEG, PNG)
		if (tipo.equals("image/jpg") || tipo.equals("application/pdf")  || tipo.equals("image/jpeg") || tipo.equals("image/png")) {
			// atualizando a foto do produto e retornando a resposta
			GetCursoDTO result = cursoService.incluirAvaliacao(id, avaliacao.getBytes());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PutMapping(value = "incluir-gabarito", consumes = { "multipart/form-data" })
	public ResponseEntity<GetCursoDTO> IncluirGabarito(@RequestParam("id") UUID id,
			@RequestParam("gabarito") MultipartFile gabarito) throws Exception {

		// capturando o tipo do arquivo
		String tipo = gabarito.getContentType();

		// verificando se o formato é válido (JPG, JPEG, PNG)
		if (tipo.equals("image/jpg") || tipo.equals("application/pdf")  || tipo.equals("image/jpeg") || tipo.equals("image/png")) {
			// atualizando a foto do produto e retornando a resposta
			GetCursoDTO result = cursoService.incluirGabarito(id, gabarito.getBytes());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}		
	
}
	
	@PutMapping(value = "incluir-material", consumes = { "multipart/form-data" })
	public ResponseEntity<GetCursoDTO> IncluirMaterial(@RequestParam("id") UUID id,
			@RequestParam("material") MultipartFile material) throws Exception {

		// capturando o tipo do arquivo
		String tipo = material.getContentType();

		// verificando se o formato é válido (JPG, JPEG, PNG)
		if (tipo.equals("image/jpg") || tipo.equals("application/pdf")  || tipo.equals("image/jpeg") || tipo.equals("image/png")) {
			// atualizando a foto do produto e retornando a resposta
			GetCursoDTO result = cursoService.incluirMaterial(id, material.getBytes());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}		
}
	
	  @GetMapping("/download-material/{material}")
	    public ResponseEntity<byte[]> downloadMaterial(@PathVariable UUID material) {
	        try {
	            byte[] CursoMaterial = cursoService.getCursoMaterial(material);

	            // Configurar os cabeçalhos da resposta, por exemplo, o tipo de conteúdo
	            // e o nome do arquivo se necessário
	            // HttpHeaders headers = new HttpHeaders();
	            // headers.setContentType(MediaType.APPLICATION_PDF);
	            // headers.setContentDispositionFormData("attachment", "proficiencia.pdf");

	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    // .headers(headers)
	                    .body(CursoMaterial);
	        } catch (Exception e) {
	            // Lidar com exceções, por exemplo, proficiência não encontrada ou dados vazios
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
		  @GetMapping("/download-avaliacao/{avaliacao}")
		    public ResponseEntity<byte[]> downloadAvaliacao(@PathVariable UUID avaliacao) {
		        try {
		            byte[] CursoAvaliacao = cursoService.getCursoAvaliacao(avaliacao);

		            // Configurar os cabeçalhos da resposta, por exemplo, o tipo de conteúdo
		            // e o nome do arquivo se necessário
		            // HttpHeaders headers = new HttpHeaders();
		            // headers.setContentType(MediaType.APPLICATION_PDF);
		            // headers.setContentDispositionFormData("attachment", "proficiencia.pdf");

		            return ResponseEntity
		                    .status(HttpStatus.OK)
		                    // .headers(headers)
		                    .body(CursoAvaliacao);
		        } catch (Exception e) {
		            // Lidar com exceções, por exemplo, proficiência não encontrada ou dados vazios
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		        }
		    }
	
			  @GetMapping("/download-gabarito/{gabarito}")
			    public ResponseEntity<byte[]> downloadGabarito(@PathVariable UUID gabarito) {
			        try {
			            byte[] CursoGabarito = cursoService.getCursoGabarito(gabarito);

			            // Configurar os cabeçalhos da resposta, por exemplo, o tipo de conteúdo
			            // e o nome do arquivo se necessário
			            // HttpHeaders headers = new HttpHeaders();
			            // headers.setContentType(MediaType.APPLICATION_PDF);
			            // headers.setContentDispositionFormData("attachment", "proficiencia.pdf");

			            return ResponseEntity
			                    .status(HttpStatus.OK)
			                    // .headers(headers)
			                    .body(CursoGabarito);
			        } catch (Exception e) {
			            // Lidar com exceções, por exemplo, proficiência não encontrada ou dados vazios
			            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			        }
			    }
		
	
}
