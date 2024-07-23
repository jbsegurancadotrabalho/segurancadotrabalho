package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.entities.map.Contato;
import br.com.jbst.DTOs.GetContatoDTOs;
import br.com.jbst.DTOs.PostContatoDTOs;
import br.com.jbst.DTOs.PutContatoDTOs;
import br.com.jbst.repositories.ContatoRepository;
import br.com.jbst.repositories.UnidadeDeTreinamentoRepository;



@Service
public class ContatoServices {


@Autowired
ModelMapper modelMapper;

@Autowired
ContatoRepository contatoRepository;

@Autowired
UnidadeDeTreinamentoRepository unidadedetreinamentoRepository;

public GetContatoDTOs criarContato(PostContatoDTOs dto) throws Exception {

	try {

		Contato contato = modelMapper.map(dto, Contato.class);
		contato.setIdContato(UUID.randomUUID());
		contato.setDataHoraCriacao(Instant.now());
		contato.setUnidadedetreinamento(unidadedetreinamentoRepository.findById(dto.getIdUnidadedetreinamento()).get());
		contatoRepository.save(contato);
		return modelMapper.map(contato, GetContatoDTOs.class);

	}

	catch (Exception e) {
		// TODO: handle exception
	}
	return null;
}

public GetContatoDTOs editarContato(PutContatoDTOs dto) throws Exception {
	Optional<Contato> contatos = contatoRepository.findById(dto.getIdContato());
	if (contatos.isEmpty())
		throw new IllegalArgumentException("Chamado inválido: " + dto.getIdContato());
	Contato contato = contatos.get();
	contato = modelMapper.map(dto, Contato.class);
	contato.setDataHoraCriacao(Instant.now());
	contato.setUnidadedetreinamento(unidadedetreinamentoRepository.findById(dto.getIdUnidadedetreinamento()).get());
	contatoRepository.save(contato);
	return modelMapper.map(contato, GetContatoDTOs.class);
}

public List<GetContatoDTOs> consultarContatos(String contato) throws Exception {
	List<Contato> contatos = contatoRepository.findAll();
	List<GetContatoDTOs> lista = modelMapper.map(contatos, new TypeToken<List<GetContatoDTOs>>() {
	}.getType());
	return lista;
} 

public GetContatoDTOs consultarUmContato(UUID idContato) {
    Optional<Contato> registro = contatoRepository.findById(idContato);

    if (registro.isPresent()) {
        Contato contato = registro.get();
        return modelMapper.map(contato, GetContatoDTOs.class);
    } else {
      
		throw new RuntimeException("Curso não encontrado"); // Lançar exceção quando não encontrada
    }
}
public GetContatoDTOs excluirContato(UUID id) throws Exception {
	Optional<Contato> contatos = contatoRepository.findById(id);
	if (contatos.isEmpty())
		throw new IllegalArgumentException("Contato inválido: " + id);
	Contato contato = contatos.get();
	contatoRepository.delete(contato);
	return modelMapper.map(contato, GetContatoDTOs.class);
}

}
