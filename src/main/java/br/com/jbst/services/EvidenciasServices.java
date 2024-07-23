package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTOs.GetEvidenciasDTOs;
import br.com.jbst.DTOs.PostEvidenciasDTOs;
import br.com.jbst.DTOs.PutEvidenciasDTOs;
import br.com.jbst.entities.Curso;
import br.com.jbst.entities.Evidencias;
import br.com.jbst.repositories.EvidenciasRepository;
import br.com.jbst.repositories.MatriculasRepository;

@Service
public class EvidenciasServices {

	@Autowired
	EvidenciasRepository evidenciasRepository;
	
	@Autowired
	MatriculasRepository matriculasRepository;
	
	
	@Autowired
	ModelMapper modelMapper;
	
	
	public GetEvidenciasDTOs criarEvidencias(PostEvidenciasDTOs dto) {
		Evidencias evidencias = modelMapper.map(dto, Evidencias.class);
		evidencias.setIdEvidencias(UUID.randomUUID());
		evidencias.setMatriculas(matriculasRepository.findById(dto.getIdMatricula()).get());
		evidencias.setDataHoraCriacao(Instant.now());
		evidenciasRepository.save(evidencias);
		return modelMapper.map(evidencias, GetEvidenciasDTOs.class);
		
    }
    
    public GetEvidenciasDTOs editarEvidencias(PutEvidenciasDTOs dto) {
		UUID id = dto.getIdEvidencias();
		Evidencias evidencias = evidenciasRepository.findById(id).orElseThrow();
		modelMapper.map(dto, evidencias );
		evidencias.setDataHoraCriacao(Instant.now());
		evidencias.setMatriculas(matriculasRepository.findById(dto.getIdMatricula()).get());
		evidenciasRepository.save(evidencias);
		return modelMapper.map(evidencias, GetEvidenciasDTOs.class);
	}
    
    public List<GetEvidenciasDTOs> consultarEvidencias(String evidencias) throws Exception {
		List<Evidencias> registro = evidenciasRepository.findAll();
		List<GetEvidenciasDTOs> lista = modelMapper.map(registro, new TypeToken<List<GetEvidenciasDTOs>>() {
		}.getType());
		return lista;
	} 
    
    
    public GetEvidenciasDTOs consultarUmaEvidencia(UUID idEvidencias) {
        Optional<Evidencias> registro = evidenciasRepository.findById(idEvidencias);
        if (registro.isPresent()) {
        	Evidencias evidencias = registro.get();
            return modelMapper.map(evidencias, GetEvidenciasDTOs.class);
        } else {
          
			throw new RuntimeException("Proficiencia não encontrado"); // Lançar exceção quando não encontrada
        }
    }
    
    
    public GetEvidenciasDTOs incluirEvidencias(UUID id, byte[] incluirevidencias) throws Exception {
		Optional<Evidencias> registro = evidenciasRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException(" inválido: " + id);
		Evidencias evidencias = registro.get();
		evidencias.setInserir_evidencias(incluirevidencias);
		evidenciasRepository.save(evidencias);
		return modelMapper.map(evidencias, GetEvidenciasDTOs.class);
	}

    public GetEvidenciasDTOs excluirEvidencias(UUID id) throws Exception  {
		Optional<Evidencias> registro = evidenciasRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException("Proficiencia não existe: " + id);
		Evidencias evidencias = registro.get();
		evidenciasRepository.delete(evidencias);
		GetEvidenciasDTOs dto = modelMapper.map(evidencias, GetEvidenciasDTOs.class);
		dto.getNome();
	    return dto;

	}  
    
    public byte[] getEvidenciasDTOs (UUID evidencias ) {
        Optional<Evidencias> registro = evidenciasRepository.findById(evidencias);

        if (registro.isPresent()) {
        	Evidencias evidencia = registro.get();

            if (evidencia.getInserir_evidencias() != null) {
                return evidencia.getInserir_evidencias();
            } else {
                throw new RuntimeException("Os dados binários da Evidênica estão vazios.");
            }
        } else {
            throw new RuntimeException("Evidência não encontrada para o ID: " + evidencias);
        }

    }
}



