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

import br.com.jbst.DTO.RelatorioFaturamentoDTO;
import br.com.jbst.DTOs.GetFaturamentoDTO;
import br.com.jbst.DTOs.PostFaturamentoDTO;
import br.com.jbst.DTOs.PutFaturamentoDTO;
import br.com.jbst.services.FaturamentoService;

@RestController
@RequestMapping(value = "/api/faturamento")
public class FaturamentoControllers {
	
	
	@Autowired
	FaturamentoService  faturamentoService;
	
	@PostMapping
	public ResponseEntity<GetFaturamentoDTO> CriarFaturamento(@RequestBody PostFaturamentoDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(faturamentoService.criarFaturamento(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetFaturamentoDTO> editarFaturamento(@RequestBody PutFaturamentoDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(faturamentoService.editarFaturamento(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetFaturamentoDTO>> ConsultarFaturamento() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(faturamentoService.consultarFaturamentos(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetFaturamentoDTO> consultarUmFaturamento(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(faturamentoService.consultarUmFaturamento(id));
	}
	
	 @GetMapping("/calcularTotal/{id}")
		public  ResponseEntity<RelatorioFaturamentoDTO> calcularEAtualizarTotal(@PathVariable("id") UUID id){
			return ResponseEntity.status(HttpStatus.OK).body(faturamentoService.calcularEAtualizarTotal(id));

		}

	@DeleteMapping("{id}")
	public ResponseEntity<GetFaturamentoDTO> ExcluirFaturamento (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(faturamentoService.excluirFaturamento(id));

	}
	


	    @PostMapping("/fechar-manualmente/{id}")
	    public ResponseEntity<String> fecharFaturaManualmente(@PathVariable UUID id) {
	        try {
	            faturamentoService.fecharFaturaManualmente(id);
	            return ResponseEntity.ok("Fatura fechada manualmente com sucesso.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Erro ao fechar manualmente a fatura: " + e.getMessage());
	        }
	    }
	    
	    @PostMapping("/abrir-manualmente/{id}")
	    public ResponseEntity<String> abrirFaturaManualmente(@PathVariable UUID id) {
	        try {
	            faturamentoService.reabrirFatura(id);
	            return ResponseEntity.ok("Fatura aberta manualmente com sucesso.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Erro ao abrir manualmente a fatura: " + e.getMessage());
	        }
	    }
	    
	 
	    
	    @GetMapping("/api/faturamentos-em-aberto/usuario/{idUsuario}")
	    public ResponseEntity<List<GetFaturamentoDTO>> buscarFaturamentosPorIdUsuariofaturaAberta(@PathVariable UUID idUsuario) {
	        List<GetFaturamentoDTO> faturamentos = faturamentoService.buscarFaturamentosPorIdUsuarioFaturaAberta(idUsuario);
	        if (faturamentos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(faturamentos, HttpStatus.OK);
	    }
	    
	    
	    @GetMapping("/api/faturamentos/{idUsuario}/{mes}/{ano}")
	    public List<GetFaturamentoDTO> buscarFaturamentosPorIdUsuarioEMesEAno(
	            @PathVariable UUID idUsuario, @PathVariable int mes, @PathVariable int ano) {
	        return faturamentoService.buscarFaturamentosPorIdUsuario(idUsuario, mes, ano);
	    }
	}
