package br.com.jbst.services;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetFormacaoDTO;
import br.com.jbst.DTO.PostFormacaoDTO;
import br.com.jbst.DTO.PutFormacaoDTO;
import br.com.jbst.entities.Instrutor;
import br.com.jbst.entities.map.Formacao;
import br.com.jbst.repositories.FormacaoRepository;
import br.com.jbst.repositories.InstrutorRepository;


@Service
public class FormacaoService {

    @Autowired
    FormacaoRepository formacaoRepository;
 
    @Autowired
    InstrutorRepository instrutorRepository;
    
    @Autowired
    ModelMapper modelMapper;

  

    public GetFormacaoDTO criarFormacao(PostFormacaoDTO dto) throws Exception {
        try {
            // Mapeia o DTO para a entidade Formacao
            Formacao formacao = modelMapper.map(dto, Formacao.class);

            // Define o ID e a data/hora de criação
            formacao.setIdFormacao(UUID.randomUUID());
            formacao.setDataHoraCriacao(Instant.now());

            // Procura o Instrutor pelo ID
            Instrutor instrutor = instrutorRepository.findById(dto.getIdinstrutor())
                    .orElseThrow(() -> new Exception("Instrutor não encontrado com ID: " + dto.getIdinstrutor()));

            // Associa o Instrutor à Formacao
            formacao.setInstrutores(instrutor);

            // Salva a Formacao no repositório
            formacaoRepository.save(formacao);

            // Retorna a Formacao mapeada para GetFormacaoDTO
            return modelMapper.map(formacao, GetFormacaoDTO.class);
        } catch (Exception e) {
            // Você deve considerar logar a exceção para rastrear erros
            e.printStackTrace();
            throw e; // Re-lança a exceção para que seja tratada em um nível superior, se necessário
        }
    }

    
    public GetFormacaoDTO editarFormacao(PutFormacaoDTO dto) {
        UUID idFormacao = dto.getIdFormacao();
        Formacao formacao = formacaoRepository.findById(idFormacao).orElseThrow();
        modelMapper.map(dto, formacao);
        formacao.setDataHoraCriacao(Instant.now());
        UUID idInstrutor = dto.getIdinstrutor();
        Instrutor instrutor = instrutorRepository.findById(idInstrutor).orElseThrow();

        // Defina o instrutor na formacao (assumindo que a propriedade instrutores seja uma lista)

        formacaoRepository.save(formacao);
        return modelMapper.map(formacao, GetFormacaoDTO.class);
    }



    public List<GetFormacaoDTO> consultarFormacao() {
		List<Formacao> formacoes = formacaoRepository.findAll();
		return formacoes.stream().map(pf -> modelMapper.map(pf, GetFormacaoDTO.class))
				.collect(Collectors.toList());
	}

    public GetFormacaoDTO consultarUmaFormacaoService(UUID idFormacao) {
        Optional<Formacao> formacaoOptional = formacaoRepository.findById(idFormacao);

        if (formacaoOptional.isPresent()) {
            Formacao formacao = formacaoOptional.get();
            return modelMapper.map(formacao, GetFormacaoDTO.class);
        } else {
          
			throw new RuntimeException("Formação não encontrada"); // Lançar exceção quando não encontrada
        }
    }

    public GetFormacaoDTO excluirFormacao(UUID id) throws Exception  {
		Optional<Formacao> formacoes = formacaoRepository.findById(id);
		if (formacoes.isEmpty())
			throw new IllegalArgumentException("Funcionario não existe: " + id);
		// capturando o funcionario do banco de dados
		Formacao formacao = formacoes.get();
		// excluindo o funcionario no banco de dados
		formacaoRepository.delete(formacao);
		GetFormacaoDTO dto = modelMapper.map(formacao, GetFormacaoDTO.class);
		dto.getFormacao();
	    return dto;

	}

}

