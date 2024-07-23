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


import br.com.jbst.DTOs.GetProficienciaDTOs;
import br.com.jbst.DTOs.PostProficienciaDTOs;
import br.com.jbst.DTOs.PutProficienciaDTOs;
import br.com.jbst.services.ProficienciaService;



@RestController
@RequestMapping(value = "/api/proficiencias")
public class ProficienciaController {
	
	
	@Autowired
	ProficienciaService  proficienciaService;
	
	@PostMapping
	public ResponseEntity<GetProficienciaDTOs> CriarProficiencia(@RequestBody PostProficienciaDTOs dto ){
		return ResponseEntity.status(HttpStatus.CREATED).body(proficienciaService.criarProficiencia(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetProficienciaDTOs> editarCurso(@RequestBody PutProficienciaDTOs dto) {
		return ResponseEntity.status(HttpStatus.OK).body(proficienciaService.editarProficiencia(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetProficienciaDTOs>> ConsultarProficiencias() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(proficienciaService.consultarProficiencias(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetProficienciaDTOs> consultarUmaProficiencia(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(proficienciaService.consultarUmaProficiencia(id));

	}
	
	
	@PutMapping(value = "incluir-proficiencia", consumes = { "multipart/form-data" })
	public ResponseEntity<GetProficienciaDTOs> IncluirProficiencia(@RequestParam("id") UUID id,
			@RequestParam("incluirproficiencia") MultipartFile incluirproficiencia) throws Exception {

		// capturando o tipo do arquivo
		String tipo = incluirproficiencia.getContentType();

		// verificando se o formato é válido (JPG, JPEG, PNG)
		if (tipo.equals("image/jpg") || tipo.equals("application/pdf")  || tipo.equals("image/jpeg") || tipo.equals("image/png")) {
			// atualizando a foto do produto e retornando a resposta
			GetProficienciaDTOs result = proficienciaService.incluirProficiencia(id, incluirproficiencia.getBytes());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	

	@DeleteMapping("{id}")
	public ResponseEntity<GetProficienciaDTOs> ExcluirProficiencia (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(proficienciaService.excluirProficiencia(id));

	}
	
    @GetMapping("/download/{proficienciaId}")
    public ResponseEntity<byte[]> downloadProficiencia(@PathVariable UUID proficienciaId) {
        try {
            byte[] proficienciaData = proficienciaService.getProficienciaData(proficienciaId);

            // Configurar os cabeçalhos da resposta, por exemplo, o tipo de conteúdo
            // e o nome do arquivo se necessário
            // HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_PDF);
            // headers.setContentDispositionFormData("attachment", "proficiencia.pdf");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    // .headers(headers)
                    .body(proficienciaData);
        } catch (Exception e) {
            // Lidar com exceções, por exemplo, proficiência não encontrada ou dados vazios
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
