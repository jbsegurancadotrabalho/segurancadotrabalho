package br.com.jbst.services;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetPedidosDTO;
import br.com.jbst.DTO.PostPedidosDTO;
import br.com.jbst.DTO.PutPedidosDTO;
import br.com.jbst.DTO.RelatorioPedidosDTO;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Pedidos;
import br.com.jbst.entities.map.Empresa;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.PedidosRepository;
import org.springframework.transaction.annotation.Transactional;



@Service
public class PedidosService {

    @Autowired
	PedidosRepository pedidosRepository;
    
    @Autowired
  	EmpresaRepository empresaRepository;
  
    @Autowired
	ModelMapper modelMapper;
    
    


    @Transactional(rollbackFor = Exception.class)
    public GetPedidosDTO criarPedidos(PostPedidosDTO dto) throws Exception {
        // Verificar se o pedido já está registrado
        if (pedidoJaRegistrado(dto.getNumerodopedido(), dto.getVenda(), dto.getNotafiscal())) {
            throw new Exception("Este pedido já foi registrado no sistema. Por favor, verifique os dados fornecidos e tente novamente.");
        }
        // Criar um novo pedido
        Pedidos pedidos = modelMapper.map(dto, Pedidos.class);
        pedidos.setIdPedidos(UUID.randomUUID());
        pedidos.setDataHoraCriacao(Instant.now());
        pedidos.setPedidoFechado(false);

        // Obter a empresa pelo ID e definir no pedido
        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + dto.getIdEmpresa()));
        pedidos.setEmpresa(empresa);
        // Salvar o novo pedido no banco de dados
        pedidosRepository.save(pedidos);
        // Mapear o pedido criado para um DTO de resposta e retornar
        return modelMapper.map(pedidos, GetPedidosDTO.class);
    }
    
    private boolean pedidoJaRegistrado(String numerodopedido, String venda, String notafiscal) {
        return pedidosRepository.existsByNumerodopedidoAndVendaAndNotafiscal(numerodopedido, venda, notafiscal);
    }



	public GetPedidosDTO  editarPedidos(PutPedidosDTO dto) {
		
		 ZoneId zone = ZoneId.systemDefault();
         // Converter para ZonedDateTime
         ZonedDateTime data_de_pagamento = ZonedDateTime.ofInstant(dto.getData_de_pagamento(), zone);
         // Obter a data atual sem informações de fuso horário
         LocalDate Data_de_pagamento = LocalDate.now(zone);
         LocalDate dataInicioAjustada = data_de_pagamento.toLocalDate();
		
		
		UUID id = dto.getIdPedidos();
		Pedidos pedidos = pedidosRepository.findById(id).orElseThrow();
		modelMapper.map(dto, pedidos );
		pedidosRepository.save(pedidos);
		return modelMapper.map(pedidos, GetPedidosDTO.class);
	}

    
    public List<GetPedidosDTO> consultarPedidos() throws Exception {
		List<Pedidos> pedidos = pedidosRepository.findAll();
		List<GetPedidosDTO> lista = modelMapper.map(pedidos, new TypeToken<List<GetPedidosDTO>>() {
		}.getType());
		return lista;
	} 
    
    
    public GetPedidosDTO  consultarPedidos(UUID idPedidos) {
        Optional<Pedidos> pedidos = pedidosRepository.findById(idPedidos);

        if (pedidos.isPresent()) {
            Pedidos pedido = pedidos.get();
            return modelMapper.map(pedido, GetPedidosDTO.class);
        } else {
          
			throw new RuntimeException("Pedido não encontrado"); // Lançar exceção quando não encontrada
        }
    }
    

    public GetPedidosDTO  excluirPedidos(UUID id) throws Exception  {
		Optional<Pedidos> pedidos = pedidosRepository.findById(id);
		if (pedidos.isEmpty())
			throw new IllegalArgumentException("Pedido não existe: " + id);
		// capturando o funcionario do banco de dados
	    Pedidos pedido = pedidos.get();
		// excluindo o funcionario no banco de dados
		pedidosRepository.delete(pedido);
		GetPedidosDTO dto = modelMapper.map(pedido, GetPedidosDTO.class);
		dto.getIdPedidos();
	    return dto;

	}
    

    public RelatorioPedidosDTO calcularEAtualizarTotal(UUID idPedidos) {
        Optional<Pedidos> pedidosOptional = pedidosRepository.findById(idPedidos);

        if (pedidosOptional.isPresent()) {
            Pedidos pedidos = pedidosOptional.get();
            BigDecimal totalValue = BigDecimal.ZERO;

            if (pedidos.getMatricula() != null && !pedidos.getMatricula().isEmpty()) {
                for (Matriculas matricula : pedidos.getMatricula()) {
                    if (matricula.getValor() != null) {
                        totalValue = totalValue.add(matricula.getValor());
                    }
                }
            }

            pedidos.setTotal(totalValue);
            pedidosRepository.save(pedidos);

            return modelMapper.map(pedidos, RelatorioPedidosDTO.class);
        } else {
            throw new RuntimeException("Pedidos não encontrado");
        }
    }



    @Transactional
    public void fecharPedidoManualmente(UUID idPedido) {
        // Buscar o pedido pelo ID
        Pedidos pedido = pedidosRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Fechar o pedido manualmente
        pedido.setPedidoFechado(true);

        // Salvar as alterações no repositório
        pedidosRepository.save(pedido);
    }

    @Transactional
    public void reabrirPedido(UUID idPedido) {
        Pedidos pedido = pedidosRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        if (!pedido.isPedidoFechado()) {
            throw new IllegalStateException("O pedido não está fechado para reabrir.");
        }

        // Reabra o pedido
        pedido.setPedidoFechado(false);
        pedido.setMatriculasBloqueadas(false);

        // Atualize outras propriedades ou execute ações necessárias ao reabrir o pedido

        pedidosRepository.save(pedido);
    }
    
    
    
    public List<GetPedidosDTO> buscarPedidosPorIdUsuarioFechado(UUID idUsuario) {
        List<GetPedidosDTO> pedidosDTO = new ArrayList<>();
        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Pedidos> pedidos = empresa.getPedidos();
            for (Pedidos pedido : pedidos) {
                if (pedido.isPedidoFechado()) { // Verifica se a fatura está aberta
                    GetPedidosDTO pedidos1DTO = modelMapper.map(pedido, GetPedidosDTO.class);
                    // Mapeie outros dados do faturamento, se necessário
                    pedidosDTO.add(pedidos1DTO);
                }
            }
        }
        return pedidosDTO;
    }


    public List<GetPedidosDTO> buscarPedidosPorIdUsuarioAbertos(UUID idUsuario) {
        List<GetPedidosDTO> pedidosDTO = new ArrayList<>();
        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Pedidos> pedidos = empresa.getPedidos();
            for (Pedidos pedido : pedidos) {
                if (!pedido.isPedidoFechado()) { // Verifica se o pedido está aberto
                    GetPedidosDTO pedidoDTO = modelMapper.map(pedido, GetPedidosDTO.class);
                    // Mapeie outros dados do pedido, se necessário
                    pedidosDTO.add(pedidoDTO);
                }
            }
        }
        return pedidosDTO;
    }

 
    public List<GetPedidosDTO> buscarPedidosPorIdUsuario(UUID idUsuario) {
        List<GetPedidosDTO> pedidosDTO = new ArrayList<>();
        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        return pedidosDTO;
    }
    
    public List<GetPedidosDTO> buscarPedidosPorIdUsuario(UUID idUsuario, int mes, int ano) {
        List<GetPedidosDTO> pedidosDTO = new ArrayList<>();
        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Pedidos> pedidos = empresa.getPedidos();
            for (Pedidos pedido : pedidos) {
                Instant dataHoraCriacao = pedido.getDataHoraCriacao();
                int pedidoMes = dataHoraCriacao.atZone(ZoneId.systemDefault()).getMonthValue();
                int pedidoAno = dataHoraCriacao.atZone(ZoneId.systemDefault()).getYear();
                if (pedidoMes == mes && pedidoAno == ano) {
                    GetPedidosDTO pedidoDTO = modelMapper.map(pedido, GetPedidosDTO.class);
                    pedidosDTO.add(pedidoDTO);
                }
            }
        }
        return pedidosDTO;
    }

    }

  





    


