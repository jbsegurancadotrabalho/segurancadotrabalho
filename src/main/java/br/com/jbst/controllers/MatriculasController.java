package br.com.jbst.controllers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.hibernate.boot.MappingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DTO.GetMatriculaDTO;
import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTO.PutTurmasInstrutor;
import br.com.jbst.MatriculasDTO.AdicionarUsuariosMatriculaDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaPedidosDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaPedidosDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaCliente;
import br.com.jbst.MatriculasDTO.PutMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaPedidosDTO;
import br.com.jbst.services.MatriculasService;

@RestController
@RequestMapping (value = "/api/matriculas")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.PUT, RequestMethod.OPTIONS})
public class MatriculasController {
	
	@Autowired
	MatriculasService matriculaService;
	

	//1
	@PostMapping("/criar-matriculas-faturamento-pj")
	public ResponseEntity<GetMatriculaFaturamentoPjDTO> criarMatriculaFaturamentoPj( @RequestBody PostMatriculaFaturamentoPjDTO dto) throws Exception {
	    return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criarMatriculaFaturamentoPj( dto));
	}
	
	//2
	@PutMapping("/editar-matriculas-faturamento-pj")
	public ResponseEntity<GetMatriculaFaturamentoPjDTO> editarMatriculas(@RequestBody PutMatriculaFaturamentoPjDTO dto) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(matriculaService.editarMatriculaPj(dto));

	}
	
	
	//3
	@PostMapping("/criar-matriculas-faturamento-pf")
	public ResponseEntity<GetMatriculaFaturamentoPfDTO> criarMatriculaFaturamentoPf( @RequestBody PostMatriculaFaturamentoPfDTO dto) throws Exception {
	    return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criarMatriculasFaturamentoPf( dto));
	}
	
	//4
	@PutMapping("/editar-matriculas-faturamento-pf")
	public ResponseEntity<GetMatriculaFaturamentoPfDTO> editarMatriculaspf(@RequestBody PutMatriculaFaturamentoPfDTO dto) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(matriculaService.editarMatriculaPf(dto));

	}
	
	//5
	@PostMapping("/criarMatricula-pedidos/{idPedidos}")
	public ResponseEntity<GetMatriculaPedidosDTO> criarMatricula(@PathVariable UUID idPedidos, @RequestBody PostMatriculaPedidosDTO  dto) throws Exception {
	    return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criarMatriculasPedidos(idPedidos, dto));
	}


	    @PutMapping("/{idMatricula}")
	    public ResponseEntity<GetMatriculaPedidosDTO> editarMatriculaPedidos(
	            @PathVariable UUID idMatricula,
	            @RequestBody PutMatriculaPedidosDTO dto) {
	        try {
	            GetMatriculaPedidosDTO matriculaAtualizada = matriculaService.editarMatriculasPedidos(idMatricula, dto);
	            return ResponseEntity.ok(matriculaAtualizada);
	        } catch (Exception e) {
	            // Trate a exceção conforme necessário
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	





	@GetMapping
	public  ResponseEntity<List<GetMatriculaDTO>> ConsultarMatriculas() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(matriculaService.consultarMatriculas(toString()));
	                                 }
	 
	
	@GetMapping("{id}")
	public  ResponseEntity<GetMatriculaDTO> consultarUmaMatricula(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(matriculaService.consultarUmaMatricula(id));

	}


	@DeleteMapping("{id}")
	public void ExcluirMatricula(@PathVariable UUID id) {

	}
	
	@PutMapping("/incluirUsuarios")
	public ResponseEntity<GetMatriculaDTO> editarUsuarios(@RequestBody AdicionarUsuariosMatriculaDTO dto) throws Exception {
	  return ResponseEntity.status(HttpStatus.OK).body(matriculaService.adicionarUsuariosMatricula(dto));
	}
	
	@DeleteMapping("/{matriculaId}/usuarios/{usuarioId}")
	public ResponseEntity<?> excluirUsuarioMatricula(
	        @PathVariable UUID matriculaId,
	        @PathVariable UUID usuarioId) {
	    try {
	        // Transforma o usuárioId em uma lista para ser compatível com o método na service
	        List<UUID> usuarioIds = Collections.singletonList(usuarioId);

	        GetMatriculaDTO matriculaDTO = matriculaService.excluirUsuariosMatricula(matriculaId, usuarioIds);
	        return ResponseEntity.ok(matriculaDTO);
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matrícula não encontrada com o ID: " + matriculaId);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir usuário da matrícula: " + e.getMessage());
	    }
	}
	
	
	
	@PutMapping("/editar-matriculas-cliente")
    public ResponseEntity<PutMatriculaCliente> editarMatricula(@RequestBody PutMatriculaCliente dto) {
        try {
            PutMatriculaCliente matriculaAtualizada = matriculaService.editarMatriculaCliente(dto);
            return ResponseEntity.ok(matriculaAtualizada);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	

	    } 


