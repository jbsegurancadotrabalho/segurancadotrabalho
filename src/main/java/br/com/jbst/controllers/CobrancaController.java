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

import br.com.jbst.CobrancasDTO.GetCobrancaFaturamentoDTO;
import br.com.jbst.CobrancasDTO.PostCobrancaFaturamentoEmpresa;
import br.com.jbst.CobrancasDTO.PostCobrancaFaturamentoPessoaFisica;
import br.com.jbst.CobrancasDTO.PostCobrancaPedidos;
import br.com.jbst.CobrancasDTO.PutCobrancaDTO;
import br.com.jbst.DTO.GetCursoDTO;
import br.com.jbst.services.CobrancaServices;

@RestController
@RequestMapping(value = "/api/cobranca")
public class CobrancaController {
	
	
	@Autowired
	CobrancaServices cobrancaService;
	
    @PostMapping("cobrança-faturamento-empresa")
	public ResponseEntity< GetCobrancaFaturamentoDTO> criarCobrancaEmpresa(@RequestBody PostCobrancaFaturamentoEmpresa dto) throws Exception {	
		return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.criarCobrancaEmpresa(dto));		
	}
    
    @PostMapping("cobrança-faturamento-pessoa-fisica")
	public ResponseEntity< GetCobrancaFaturamentoDTO> criarCobrancaPessoaFisica(@RequestBody PostCobrancaFaturamentoPessoaFisica dto) throws Exception {	
		return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.criarCobrancaPessoaFisica(dto));		
	}
    
    
    @PostMapping("/criar-cobranca-pedidos")
    public ResponseEntity<GetCobrancaFaturamentoDTO> criarCobrancaPedidos(@RequestBody PostCobrancaPedidos dto) {
        try {
            GetCobrancaFaturamentoDTO result = cobrancaService.criarCobrancaPedidos(dto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            // Logar a exceção ou retornar um ResponseEntity com um código de erro específico
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PutMapping("editar-cobranca")
	public ResponseEntity< GetCobrancaFaturamentoDTO> editarCobrancas(@RequestBody PutCobrancaDTO dto) throws Exception {	
		return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.editarCobranca(dto));		
	}

	@GetMapping
	public  ResponseEntity<List<GetCobrancaFaturamentoDTO>> ConsultarCobrancas() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(cobrancaService.consultarCobrancas(toString()));
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetCobrancaFaturamentoDTO> consultarUmaCobranca(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(cobrancaService.consultarUmaCobranca(id));

	}
    
	@DeleteMapping("{id}")
	ResponseEntity<GetCobrancaFaturamentoDTO> ExcluirCobranca (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(cobrancaService.excluirCobranca(id));

	}
}
