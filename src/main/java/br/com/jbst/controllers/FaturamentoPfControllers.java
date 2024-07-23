package br.com.jbst.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

import br.com.jbst.DTO.RelatorioFaturamentoPfDto;
import br.com.jbst.DTOs.GetFaturamentoDTO;
import br.com.jbst.DTOs.GetFaturamentopfDto;
import br.com.jbst.DTOs.PostFaturamentopfDto;
import br.com.jbst.DTOs.PutFaturamentopfDto;
import br.com.jbst.MatriculasDTO.GetFaturamentoPessoaFisicaDTO;
import br.com.jbst.services.FaturamentopfServices;

@RestController
@RequestMapping(value = "/api/faturamentopf")
public class FaturamentoPfControllers {

	
	
	
	@Autowired
	FaturamentopfServices  faturamentopfServices;
	
	@PostMapping
	public ResponseEntity<GetFaturamentopfDto> CriarFaturamentoPf(@RequestBody PostFaturamentopfDto dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(faturamentopfServices.criarFaturamentoPf(dto));		

	}
	
	@PutMapping
	public ResponseEntity<GetFaturamentopfDto> editarFaturamento(@RequestBody PutFaturamentopfDto dto) {
		return ResponseEntity.status(HttpStatus.OK).body(faturamentopfServices.editarFaturamentopf(dto));

	}

	@GetMapping 
	public  ResponseEntity<List<GetFaturamentopfDto>> ConsultarFaturamento() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(faturamentopfServices.consultarFaturamentospf(toString()));
	                                 }
	


	@GetMapping("{id}")
	public  ResponseEntity<GetFaturamentopfDto> consultarUmFaturamentoPf(@PathVariable("id") UUID id) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(faturamentopfServices.consultarUmFaturamento(id));
	}
	
	 @GetMapping("/calcularTotal/{id}")
		public  ResponseEntity<RelatorioFaturamentoPfDto> CalcularEAtualizarTotal(@PathVariable("id") UUID id){
			return ResponseEntity.status(HttpStatus.OK).body(((FaturamentopfServices) faturamentopfServices).calcularEAtualizarTotal(id));

		}

	@DeleteMapping("{id}")
	public ResponseEntity<GetFaturamentopfDto> ExcluirFaturamento (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(faturamentopfServices.excluirFaturamentopf(id));

	}
	
	@PutMapping("/fechar-fatura/{idFaturamento}")
    public ResponseEntity<String> fecharFaturaManualmente(@PathVariable UUID idFaturamento) {
        try {
            faturamentopfServices.fecharFaturaManualmente(idFaturamento);
            return new ResponseEntity<>("Fatura fechada manualmente com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao fechar fatura manualmente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reabrir-fatura/{idFaturamento}")
    public ResponseEntity<String> reabrirFatura(@PathVariable UUID idFaturamento) {
        try {
            faturamentopfServices.reabrirFatura(idFaturamento);
            return new ResponseEntity<>("Fatura reaberta com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao reabrir fatura: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    


}
