package br.com.jbst.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTOs.GetFuncaoDTOs;
import br.com.jbst.entities.map.Funcao;
import br.com.jbst.repositories.FuncaoRepository;


@Service
public class FuncaoService {

	
	  @Autowired
		FuncaoRepository funcaoRepository;
	  
	    @Autowired
		ModelMapper modelMapper;
	    
	    public List<GetFuncaoDTOs> consultarFuncao(String funcao) throws Exception {
			List<Funcao> funcoes = funcaoRepository.findAll();
			List<GetFuncaoDTOs> lista = modelMapper.map(funcoes, new TypeToken<List<GetFuncaoDTOs>>() {
			}.getType());
			return lista;
		} 
	    
	    
	    public GetFuncaoDTOs consultarUmaFuncao(UUID idFuncao) {
	        Optional<Funcao> registro = funcaoRepository.findById(idFuncao);

	        if (registro.isPresent()) {
	            Funcao funcao = registro.get();
	            return modelMapper.map(funcao, GetFuncaoDTOs.class);
	        } else {
	          
				throw new RuntimeException("Funcao não encontrado"); // Lançar exceção quando não encontrada
	        }
	    }
	    

}
