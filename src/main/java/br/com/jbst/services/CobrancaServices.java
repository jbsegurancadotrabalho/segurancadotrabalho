package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.CobrancasDTO.GetCobrancaFaturamentoDTO;
import br.com.jbst.CobrancasDTO.PostCobrancaFaturamentoEmpresa;
import br.com.jbst.CobrancasDTO.PostCobrancaFaturamentoPessoaFisica;
import br.com.jbst.CobrancasDTO.PostCobrancaPedidos;
import br.com.jbst.CobrancasDTO.PutCobrancaDTO;
import br.com.jbst.DTO.GetCursoDTO;
import br.com.jbst.entities.Cobranca;
import br.com.jbst.entities.Curso;
import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.FaturamentoPf;
import br.com.jbst.entities.Pedidos;
import br.com.jbst.repositories.CobrancaRepository;
import br.com.jbst.repositories.FaturamentoRepository;
import br.com.jbst.repositories.FaturamentopfRepository;
import br.com.jbst.repositories.PedidosRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CobrancaServices {
	
	
	   @Autowired
		FaturamentoRepository faturamentoRepository;
	    
	   @Autowired
	   FaturamentopfRepository faturamentopfRepository;
	    
   
	   
	   @Autowired
	   PedidosRepository pedidosRepository; 
	   
	   @Autowired
		CobrancaRepository cobrancaRepository;
	  
	    @Autowired
		ModelMapper modelMapper;
	    
	    @Transactional
	    public  GetCobrancaFaturamentoDTO criarCobrancaEmpresa(PostCobrancaFaturamentoEmpresa dto) throws Exception {
	        try {
	            // Mapeia o DTO para a entidade Formacao
	            Cobranca cobranca = modelMapper.map(dto, Cobranca.class);

	            // Define o ID e a data/hora de criação
	            cobranca.setIdCobranca(UUID.randomUUID());
	            cobranca.setDataHoraCriacao(Instant.now());

	            // Procura o Instrutor pelo ID
	            Faturamento faturamento = faturamentoRepository.findById(dto.getIdfaturamento())
	                    .orElseThrow(() -> new Exception("Faturamento não encontrado com ID: " + dto.getIdfaturamento()));

	            // Associa o Instrutor à Formacao
	            cobranca.setFaturamento(faturamento);

	            // Salva a Formacao no repositório
	            cobrancaRepository.save(cobranca);

	            // Retorna a Formacao mapeada para GetFormacaoDTO
	            return modelMapper.map(cobranca, GetCobrancaFaturamentoDTO.class);
	        } catch (Exception e) {
	            // Você deve considerar logar a exceção para rastrear erros
	            e.printStackTrace();
	            throw e; // Re-lança a exceção para que seja tratada em um nível superior, se necessário
	        }
	    }
	    
	    @Transactional
	    public  GetCobrancaFaturamentoDTO criarCobrancaPessoaFisica(PostCobrancaFaturamentoPessoaFisica dto) throws Exception {
	        try {
	            // Mapeia o DTO para a entidade Formacao
	            Cobranca cobranca = modelMapper.map(dto, Cobranca.class);

	            // Define o ID e a data/hora de criação
	            cobranca.setIdCobranca(UUID.randomUUID());
	            cobranca.setDataHoraCriacao(Instant.now());

	            // Procura o Instrutor pelo ID
	            FaturamentoPf faturamentopf = faturamentopfRepository.findById(dto.getIdfaturamentopf())
	                    .orElseThrow(() -> new Exception("Faturamento não encontrado com ID: " + dto.getIdfaturamentopf()));

	            // Associa o Instrutor à Formacao
	            cobranca.setFaturamentoPf(faturamentopf);

	            // Salva a Formacao no repositório
	            cobrancaRepository.save(cobranca);

	            // Retorna a Formacao mapeada para GetFormacaoDTO
	            return modelMapper.map(cobranca, GetCobrancaFaturamentoDTO.class);
	        } catch (Exception e) {
	            // Você deve considerar logar a exceção para rastrear erros
	            e.printStackTrace();
	            throw e; // Re-lança a exceção para que seja tratada em um nível superior, se necessário
	        }
	    }
	    
	    @Transactional
	    public  GetCobrancaFaturamentoDTO criarCobrancaPedidos(PostCobrancaPedidos dto) throws Exception {
	        try {
	            // Mapeia o DTO para a entidade Formacao
	            Cobranca cobranca = modelMapper.map(dto, Cobranca.class);

	            // Define o ID e a data/hora de criação
	            cobranca.setIdCobranca(UUID.randomUUID());
	            cobranca.setDataHoraCriacao(Instant.now());

	            // Procura o Instrutor pelo ID
	            Pedidos pedidos = pedidosRepository.findById(dto.getIdPedidos())
	                    .orElseThrow(() -> new Exception("Pedido não encontrado com ID: " + dto.getIdPedidos()));

	            // Associa o Instrutor à Formacao
	            cobranca.setPedidos(pedidos);

	            // Salva a Formacao no repositório
	            cobrancaRepository.save(cobranca);

	            // Retorna a Formacao mapeada para GetFormacaoDTO
	            return modelMapper.map(cobranca, GetCobrancaFaturamentoDTO.class);
	        } catch (Exception e) {
	            // Você deve considerar logar a exceção para rastrear erros
	            e.printStackTrace();
	            throw e; // Re-lança a exceção para que seja tratada em um nível superior, se necessário
	        }
	    }

	    @Transactional
	    public GetCobrancaFaturamentoDTO editarCobranca(PutCobrancaDTO dto) throws Exception {
	        try {
	            // Carrega a entidade existente do banco de dados
	            Cobranca cobrancaExistente = cobrancaRepository.findById(dto.getIdCobranca())
	                    .orElseThrow(() -> new EntityNotFoundException("Cobranca não encontrada"));

	            // Atualiza apenas os campos necessários
	            cobrancaExistente.setResponsavelCobranca(dto.getResponsavelCobranca());
	            cobrancaExistente.setResponsavelCliente(dto.getResponsavelCliente());
	            cobrancaExistente.setData_de_agendamento_pagamento(dto.getData_de_agendamento_pagamento());
	            cobrancaExistente.setAssunto(dto.getAssunto());
	            cobrancaExistente.setObservacoes(dto.getObservacoes());

	            // Salva a Cobranca no repositório
	            cobrancaRepository.save(cobrancaExistente);

	            // Retorna a Cobranca mapeada para GetCobrancaFaturamentoDTO
	            return modelMapper.map(cobrancaExistente, GetCobrancaFaturamentoDTO.class);
	        } catch (Exception e) {
	            // Loga a exceção para rastrear erros
	            e.printStackTrace();
	            throw e; // Re-lança a exceção para que seja tratada em um nível superior, se necessário
	        }
	    }
	    public List<GetCobrancaFaturamentoDTO> consultarCobrancas(String cobranca) throws Exception {
			List<Cobranca> cobrancas = cobrancaRepository.findAll();
			List<GetCobrancaFaturamentoDTO> lista = modelMapper.map(cobrancas, new TypeToken<List<GetCobrancaFaturamentoDTO>>() {
			}.getType());
			return lista;
		} 
	    
	    
	    public GetCobrancaFaturamentoDTO consultarUmaCobranca(UUID idCobranca) {
	        Optional<Cobranca> cobranca = cobrancaRepository.findById(idCobranca);

	        if (cobranca.isPresent()) {
	        	Cobranca registro = cobranca.get();
	            return modelMapper.map(registro, GetCobrancaFaturamentoDTO.class);
	        } else {
	          
				throw new RuntimeException("Cobrança não encontrada"); // Lançar exceção quando não encontrada
	        }
	    }
	    
	    public GetCobrancaFaturamentoDTO excluirCobranca(UUID id) throws Exception  {
			Optional<Cobranca> cobranca = cobrancaRepository.findById(id);
			if (cobranca.isEmpty())
				throw new IllegalArgumentException("Cobrança não existe: " + id);
			// capturando o funcionario do banco de dados
		    Cobranca registro = cobranca.get();
			// excluindo o funcionario no banco de dados
			cobrancaRepository.delete(registro);
			GetCobrancaFaturamentoDTO dto = modelMapper.map(registro, GetCobrancaFaturamentoDTO.class);
			dto.getIdCobranca();
		    return dto;

		}
	    
	    
	    
}
