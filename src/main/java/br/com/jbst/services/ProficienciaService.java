package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTOs.GetProficienciaDTOs;
import br.com.jbst.DTOs.PostProficienciaDTOs;
import br.com.jbst.DTOs.PutProficienciaDTOs;
import br.com.jbst.entities.Proficiencia;
import br.com.jbst.repositories.InstrutorRepository;
import br.com.jbst.repositories.ProficienciaRepository;



@Service
public class ProficienciaService {
	   
	@Autowired
	ProficienciaRepository proficienciaRepository;
	
	@Autowired
	InstrutorRepository instrutorRepository;
	
	
	@Autowired
	ModelMapper modelMapper;
	
	
	public GetProficienciaDTOs criarProficiencia(PostProficienciaDTOs dto) {
    	Proficiencia proficiencia = modelMapper.map(dto, Proficiencia.class);
    	proficiencia.setIdProficiencia(UUID.randomUUID());
    	proficiencia.setInstrutores(instrutorRepository.findById(dto.getIdinstrutor()).get());
    	proficiencia.setDataHoraCriacao(Instant.now());
    	proficienciaRepository.save(proficiencia);
		return modelMapper.map(proficiencia, GetProficienciaDTOs.class);
		
    }
    
    public GetProficienciaDTOs editarProficiencia(PutProficienciaDTOs dto) {
		UUID id = dto.getIdProficiencia();
		Proficiencia proficiencia = proficienciaRepository.findById(id).orElseThrow();
		modelMapper.map(dto, proficiencia );
		proficiencia.setDataHoraCriacao(Instant.now());
    	proficiencia.setInstrutores(instrutorRepository.findById(dto.getIdinstrutor()).get());
		proficienciaRepository.save(proficiencia);
		return modelMapper.map(proficiencia, GetProficienciaDTOs.class);
	}
    public List<GetProficienciaDTOs> consultarProficiencias(String curso) throws Exception {
		List<Proficiencia> proficiencia = proficienciaRepository.findAll();
		List<GetProficienciaDTOs> lista = modelMapper.map(proficiencia, new TypeToken<List<GetProficienciaDTOs>>() {
		}.getType());
		return lista;
	} 
    
    
    public GetProficienciaDTOs consultarUmaProficiencia(UUID idProficiencia) {
        Optional<Proficiencia> registro = proficienciaRepository.findById(idProficiencia);
        if (registro.isPresent()) {
        	Proficiencia proficiencia = registro.get();
            return modelMapper.map(proficiencia, GetProficienciaDTOs.class);
        } else {
          
			throw new RuntimeException("Proficiencia não encontrado"); // Lançar exceção quando não encontrada
        }
    }
    
    
    public GetProficienciaDTOs incluirProficiencia(UUID id, byte[] inserirproficiencia) throws Exception {
		Optional<Proficiencia> registro = proficienciaRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException(" inválido: " + id);
		Proficiencia proficiencia = registro.get();
		proficiencia.setInserir_proficiencia(inserirproficiencia);
		proficienciaRepository.save(proficiencia);
		return modelMapper.map(proficiencia, GetProficienciaDTOs.class);
	}

    public GetProficienciaDTOs excluirProficiencia(UUID id) throws Exception  {
		Optional<Proficiencia> registro = proficienciaRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException("Proficiencia não existe: " + id);
		Proficiencia proficiencia = registro.get();
		proficienciaRepository.delete(proficiencia);
		GetProficienciaDTOs dto = modelMapper.map(proficiencia, GetProficienciaDTOs.class);
		dto.getProficiencia();
	    return dto;

	}
    
    


    public byte[] getProficienciaData(UUID proficienciaId) {
        Optional<Proficiencia> proficienciaOptional = proficienciaRepository.findById(proficienciaId);

        if (proficienciaOptional.isPresent()) {
            Proficiencia proficiencia = proficienciaOptional.get();

            if (proficiencia.getInserir_proficiencia() != null) {
                return proficiencia.getInserir_proficiencia();
            } else {
                throw new RuntimeException("Os dados binários da proficiência estão vazios.");
            }
        } else {
            throw new RuntimeException("Proficiência não encontrada para o ID: " + proficienciaId);
        }
    }
}



