package br.com.jbst.controllers;

import java.io.IOException;
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

import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTO.GetTurmasDTOs;
import br.com.jbst.DTO.PostTurmasDTO;
import br.com.jbst.DTO.PutTurmasDTO;
import br.com.jbst.DTO.PutTurmasInstrutor;
import br.com.jbst.DTO.TurmasAssinaturaInstrutorDTO;
import br.com.jbst.MatriculasDTO.DocumentoRequest;
import br.com.jbst.services.AssinaturaImportService;
import br.com.jbst.services.TurmasService;

@RestController
@RequestMapping (value = "/api/turmas")
public class TurmasController {

	
	@Autowired TurmasService turmasService;
	  @Autowired private AssinaturaImportService assinaturaImportService;
	
	@PostMapping
	public ResponseEntity<GetTurmasDTO> criarTurmaComCofre(@RequestBody PostTurmasDTO dto) throws Exception {
        GetTurmasDTO turmaCriada = turmasService.criarTurmas(dto);
        return ResponseEntity.ok(turmaCriada);
    }


@GetMapping
public  ResponseEntity<List<GetTurmasDTOs>> ConsultarTurmas() throws Exception{
	return  ResponseEntity
			.status(HttpStatus.OK)
			.body(turmasService.consultarTurmas(toString()));
                                 }
@GetMapping("{id}")
public  ResponseEntity<GetTurmasDTOs> consultarUmInstrutor(@PathVariable("id") UUID id)throws Exception{
	return ResponseEntity.status(HttpStatus.OK).body(turmasService.consultarTurmasPorId(id));

}

@GetMapping("/{id}/assinaturas-instrutores")
public ResponseEntity<TurmasAssinaturaInstrutorDTO> buscarTurmaComAssinaturasInstrutores(@PathVariable UUID id) {
    try {
        TurmasAssinaturaInstrutorDTO dto = turmasService.buscarTurmasPorId(id);
        return ResponseEntity.ok(dto);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}

@PutMapping
public ResponseEntity<GetTurmasDTO> editarTurmas(@RequestBody PutTurmasDTO dto)throws Exception {
	return ResponseEntity.status(HttpStatus.OK).body(turmasService.editarTurmas(dto));

}

@PutMapping("{incluir-instrutor}")
public ResponseEntity<GetTurmasDTO> editarTurmas(@RequestBody PutTurmasInstrutor dto) throws Exception {
  return ResponseEntity.status(HttpStatus.OK).body(turmasService.incluirInstrutor(dto));
}


@DeleteMapping("{id}")
public ResponseEntity<GetTurmasDTOs> ExcluirTurmas(@PathVariable("id") UUID id) throws Exception {
	return ResponseEntity
			.status(HttpStatus.OK)
			.body(turmasService.excluirTurmas(id));

}

@DeleteMapping("/{idTurmas}/instrutores/{idInstrutor}")
public ResponseEntity<GetTurmasDTO> excluirInstrutor(
    @PathVariable UUID idTurmas,
    @PathVariable UUID idInstrutor
) throws Exception {
    GetTurmasDTO turmasDTO = turmasService.excluirInstrutor(idTurmas, idInstrutor);
    return ResponseEntity.ok(turmasDTO);
}

@PostMapping("/abrir/{turmaId}")
public ResponseEntity<String> abrirTurma(@PathVariable UUID turmaId) {
    try {
        turmasService.turmaAberta(turmaId);
        return ResponseEntity.ok("Turma aberta com sucesso.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao abrir a turma: " + e.getMessage());
    }
}

@PostMapping("/fechar/{turmaId}")
public ResponseEntity<String> fecharTurma(@PathVariable UUID turmaId) {
    try {
        turmasService.turmaFechada(turmaId);
        return ResponseEntity.ok("Turma fechada com sucesso.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao fechar a turma: " + e.getMessage());
    }
}


@PostMapping("/{idTurma}/importar-assinaturas")
public ResponseEntity<Void> importarAssinaturas(
        @PathVariable("idTurma") UUID idTurma,
        @RequestBody DocumentoRequest request) throws IOException {

    assinaturaImportService.importarAssinaturasComoImagens(idTurma, request.getUuidDocumentoD4Sign());
    return ResponseEntity.ok().build();
}



}


