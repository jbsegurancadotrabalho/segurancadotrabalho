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

import br.com.jbst.DTO.GetPedidosDTO;
import br.com.jbst.DTO.PostPedidosDTO;
import br.com.jbst.DTO.PutPedidosDTO;
import br.com.jbst.DTO.RelatorioPedidosDTO;
import br.com.jbst.DTOs.GetFaturamentoDTO;
import br.com.jbst.entities.Pedidos;
import br.com.jbst.services.PedidosService;

@RestController
@RequestMapping (value = "/api/pedidos-de-compras")
public class PedidosController {
	
	
	@Autowired
	PedidosService  pedidosService;
	
	@PostMapping
	public ResponseEntity<GetPedidosDTO> CriarCurso(@RequestBody PostPedidosDTO dto ) throws Exception{
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.criarPedidos(dto));		

	}
	

	@PutMapping
	public ResponseEntity<GetPedidosDTO> editarPedidos(@RequestBody PutPedidosDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.editarPedidos(dto));

	}

	@GetMapping
	public  ResponseEntity<List<GetPedidosDTO>> ConsultarPedidos() throws Exception{
		return  ResponseEntity
				.status(HttpStatus.OK)
				.body(pedidosService.consultarPedidos());
	                                 }

	@GetMapping("{id}")
	public  ResponseEntity<GetPedidosDTO> consultarUmPedidos(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.consultarPedidos(id));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<GetPedidosDTO> ExcluirPedido (@PathVariable("id") UUID id) throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(pedidosService.excluirPedidos(id));

	}
	
	 @GetMapping("/calcularTotal/{id}")
		public  ResponseEntity<RelatorioPedidosDTO> calcularEAtualizarTotal(@PathVariable("id") UUID id){
			return ResponseEntity.status(HttpStatus.OK).body(pedidosService.calcularEAtualizarTotal(id));

		}

	

	 @PutMapping("/fechar/{idPedido}")
	    public ResponseEntity<String> fecharPedidoManualmente(@PathVariable UUID idPedido) {
	        try {
	            pedidosService.fecharPedidoManualmente(idPedido);
	            return new ResponseEntity<>("Pedido fechado com sucesso", HttpStatus.OK);
	        } catch (IllegalArgumentException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Erro ao fechar o pedido", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @PutMapping("/reabrir/{idPedido}")
	    public ResponseEntity<String> reabrirPedido(@PathVariable UUID idPedido) {
	        try {
	            pedidosService.reabrirPedido(idPedido);
	            return ResponseEntity.ok("Pedido reaberto com sucesso.");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        } catch (IllegalStateException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao reabrir o pedido.");
	        }
	    }

	    
	    @GetMapping("/api/pedidos-fechado/usuario/{idUsuario}")
	    public ResponseEntity<List<GetPedidosDTO>> buscarPedidosPorIdUsuarioFechado(@PathVariable UUID idUsuario) {
	        List<GetPedidosDTO> pedidos = pedidosService.buscarPedidosPorIdUsuarioFechado(idUsuario);
	        if (pedidos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(pedidos, HttpStatus.OK);
	    }
	    
	    
	    @GetMapping("/api/pedidos-aberto/usuario/{idUsuario}")
	    public ResponseEntity<List<GetPedidosDTO>> buscarPedidosPorIdUsuarioAbertos(@PathVariable UUID idUsuario) {
	        List<GetPedidosDTO> pedidos = pedidosService.buscarPedidosPorIdUsuarioAbertos(idUsuario);
	        if (pedidos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(pedidos, HttpStatus.OK);
	    }
	    
	    @GetMapping("/api/todos-pedidos-por-usuario/usuario/{idUsuario}")
	    public ResponseEntity<List<GetPedidosDTO>> buscarPedidosPorIdUsuario(@PathVariable UUID idUsuario) {
	        List<GetPedidosDTO> pedidos = pedidosService.buscarPedidosPorIdUsuario(idUsuario);
	        if (pedidos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(pedidos, HttpStatus.OK);
	    }
	    
	    @GetMapping("/usuario/{idUsuario}/{mes}/{ano}")
	    public ResponseEntity<List<GetPedidosDTO>> buscarPedidosPorIdUsuarioMesAno(
	            @PathVariable UUID idUsuario, @PathVariable int mes, @PathVariable int ano) {
	        List<GetPedidosDTO> pedidos = pedidosService.buscarPedidosPorIdUsuario(idUsuario, mes, ano);
	        if (pedidos.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(pedidos);
	    }
	}


